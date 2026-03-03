document.addEventListener("DOMContentLoaded", () => {
    const idMat = localStorage.getItem("materiaSeleccionada");
    
    // Seguridad: si no hay materia, al dashboard
    if (!idMat) {
        window.location.href = "dashboard.html";
        return;
    }
    
    cargarTareas(idMat);
});

/**
 * Obtiene las tareas, calcula estadísticas y renderiza las cards.
 */
function cargarTareas(id) {
    fetch(`/api/tareas/materia/${id}`)
    .then(res => res.json())
    .then(tareas => {
        const cont = document.getElementById("contenedorTareas");
        const tituloHeader = document.getElementById("nombreMateria");
        const statsDiv = document.getElementById("statsTareas");
        
        cont.innerHTML = "";

        // Si no hay tareas, ocultamos estadísticas y mostramos mensaje amigable
        if (tareas.length === 0) {
            tituloHeader.textContent = "Sin tareas pendientes";
            if(statsDiv) statsDiv.style.setProperty("display", "none", "important");
            cont.innerHTML = `
                <div class="col-12 text-center text-muted my-5">
                    <i class="bi bi-emoji-smile fs-1 d-block mb-3"></i>
                    <p>¡Todo al día! No hay tareas para esta materia.</p>
                </div>`;
            return;
        }

        // --- LÓGICA DE CONTADORES Y HEADER ---
        tituloHeader.textContent = `Tareas de ${tareas[0].materia.nombre}`;
        if(statsDiv) statsDiv.style.setProperty("display", "flex", "important");

        const total = tareas.length;
        const completadas = tareas.filter(t => t.estado === "COMPLETADA").length;
        const pendientes = total - completadas;

        document.getElementById("totalTareas").textContent = total;
        document.getElementById("pendientesTareas").textContent = pendientes;
        document.getElementById("completadasTareas").textContent = completadas;
        // -------------------------------------

        // --- RENDERIZADO BLINDADO DE TAREAS ---
        tareas.forEach(t => {
            const esCompletada = t.estado === "COMPLETADA";

            // 1. Elegimos la clase del badge dinámicamente
            const badgeClass = esCompletada ? 'bg-completada' : 'bg-warning-subtle text-warning';
            
            // 2. Definimos el botón o el check de terminada
            const accionHTML = !esCompletada 
                ? `<button onclick="completarTarea(${t.idTarea})" class="btn btn-primary rounded-pill px-4 shadow-sm">
                     <i class="bi bi-send me-1"></i> Entregar
                   </button>` 
                : `<span class="badge bg-completada rounded-pill px-3 py-2">
                     <i class="bi bi-check-all me-1"></i> Terminada
                   </span>`;

            const col = document.createElement("div");
            col.className = "col-md-10 col-lg-8 mb-3";
            col.innerHTML = `
                <div class="card card-pro shadow-sm p-4 border-0 ${esCompletada ? 'opacity-75' : ''}" style="border-radius: 15px;">
                    <div class="d-flex align-items-center justify-content-between flex-wrap gap-3">
                        <div style="flex: 1; min-width: 250px;">
                            <span class="badge rounded-pill mb-2 ${badgeClass}" style="font-size: 0.75rem;">
                                <i class="bi ${esCompletada ? 'bi-check-circle-fill' : 'bi-clock'} me-1"></i> ${t.estado}
                            </span>
                            
                            <h5 class="fw-bold text-dark mb-1 ${esCompletada ? 'text-decoration-line-through text-muted' : ''}">
                                ${t.titulo}
                            </h5>
                            
                            <p class="text-muted small mb-2">${t.descripcion || 'Sin descripción'}</p>
                            <div class="text-secondary small">
                                <i class="bi bi-calendar-event me-1"></i> Entrega: <strong>${t.fechaEntrega}</strong>
                            </div>
                        </div>
                        <div class="text-end">
                            ${accionHTML}
                        </div>
                    </div>
                </div>
            `;
            cont.appendChild(col);
        });
    })
    .catch(err => {
        console.error("Error al cargar tareas:", err);
        document.getElementById("contenedorTareas").innerHTML = "<p class='text-danger text-center'>Error al conectar con el servidor.</p>";
    });
}

/**
 * Cambia el estado de una tarea a COMPLETADA (PUT)
 */
function completarTarea(idTarea) {
    Swal.fire({
        title: '¿Tarea terminada?',
        text: "¡Se marcará como completada!",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#6366f1',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, ¡terminada!',
        cancelButtonText: 'Aún no'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/api/tareas/${idTarea}/completar`, { method: 'PUT' })
            .then(res => {
                if (res.ok) {
                    Swal.fire({
                        title: '¡Excelente!',
                        text: 'Tarea actualizada ✅',
                        icon: 'success',
                        timer: 1000,
                        showConfirmButton: false
                    }).then(() => location.reload());
                } else {
                    throw new Error("Error en la actualización");
                }
            })
            .catch(err => {
                console.error(err);
                Swal.fire('Error', 'No se pudo actualizar la tarea', 'error');
            });
        }
    });
}

/**
 * Crea una nueva tarea (POST)
 */
function guardarTarea() {
    const titulo = document.getElementById("nuevoTituloTarea").value;
    const descripcion = document.getElementById("nuevaDescTarea").value;
    const fecha = document.getElementById("nuevaFechaTarea").value;
    const materiaId = localStorage.getItem("materiaSeleccionada");

    if (!titulo || !fecha) {
        Swal.fire('¡Faltan datos!', 'Título y fecha son obligatorios', 'warning');
        return;
    }

    const tareaObj = {
        titulo: titulo,
        descripcion: descripcion,
        fechaEntrega: fecha,
        estado: "PENDIENTE",
        materia: { idMateria: parseInt(materiaId) }
    };

    fetch("/api/tareas", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(tareaObj)
    })
    .then(res => {
        if (res.ok) {
            const modalElement = document.getElementById('modalTarea');
            const modalInstance = bootstrap.Modal.getInstance(modalElement);
            if (modalInstance) modalInstance.hide();

            Swal.fire({ 
                title: '¡Éxito!', 
                text: 'Tarea creada correctamente', 
                icon: 'success', 
                timer: 1000, 
                showConfirmButton: false 
            }).then(() => location.reload());
        } else {
            throw new Error("Error al guardar");
        }
    })
    .catch(err => {
        console.error(err);
        Swal.fire('Error', 'Hubo un problema al guardar la tarea', 'error');
    });
}

function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}