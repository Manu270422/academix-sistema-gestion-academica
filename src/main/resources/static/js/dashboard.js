document.addEventListener("DOMContentLoaded", () => {
    const nombre = localStorage.getItem("usuarioNombre");
    const id = localStorage.getItem("usuarioId");

    // Si no hay ID, no hay sesión: de vuelta al inicio
    if (!id) {
        window.location.href = "index.html";
        return;
    }

    // Ponemos el nombre del usuario en el nav/header
    const bienvenida = document.getElementById("bienvenida");
    if (bienvenida) {
        bienvenida.textContent = `Hola, ${nombre}`;
    }

    cargarMaterias(id);
});

/**
 * Trae las materias desde el servidor y las dibuja en el Dashboard
 */
function cargarMaterias(id) {
    fetch(`/api/materias/usuario/${id}`)
        .then(res => res.json())
        .then(materias => {
            const contenedor = document.getElementById("listaMaterias");
            
            if (!materias || materias.length === 0) {
                contenedor.innerHTML = '<div class="col-12 text-center text-muted my-5">No tienes materias aún. ¡Crea la primera!</div>';
                return;
            }

            contenedor.innerHTML = ""; // Limpiamos el contenedor
            
            materias.forEach(m => {
                const col = document.createElement("div");
                col.className = "col-md-6 col-lg-4 mb-4";
                
                // --- RENDERIZADO DE LA TARJETA CON BOTÓN ELIMINAR ---
                col.innerHTML = `
                    <div class="card card-pro shadow-sm p-4 h-100 border-0" style="border-radius: 20px;">
                        <div class="d-flex justify-content-between align-items-start mb-3">
                            <div class="d-flex align-items-center">
                                <div class="p-3 bg-primary bg-opacity-10 rounded-4 me-3 text-primary">
                                    <i class="bi bi-book fs-3"></i>
                                </div>
                                <h5 class="fw-bold mb-0">${m.nombre}</h5>
                            </div>
                            <button class="btn btn-outline-danger btn-sm border-0 rounded-circle" 
                                    onclick="eliminarMateria(${m.idMateria}, '${m.nombre}')"
                                    title="Eliminar materia">
                                <i class="bi bi-trash3"></i>
                            </button>
                        </div>
                        
                        <p class="text-muted small mb-4">${m.descripcion || 'Sin descripción disponible.'}</p>
                        
                        <button onclick="verTareas(${m.idMateria})" class="btn btn-primary w-100 rounded-pill mt-auto">
                            Ver Tareas <i class="bi bi-arrow-right ms-2"></i>
                        </button>
                    </div>
                `;
                contenedor.appendChild(col);
            });
        })
        .catch(err => {
            console.error("Error al cargar materias:", err);
        });
}

/**
 * Guarda una nueva materia enviando el objeto al backend
 */
function guardarMateria() {
    const nombre = document.getElementById("nuevoNombreMat").value;
    const descripcion = document.getElementById("nuevaDescMat").value;
    const usuarioId = localStorage.getItem("usuarioId");

    if (!nombre) {
        Swal.fire('¡Ojo!', 'El nombre es obligatorio', 'warning');
        return;
    }

    const materiaObj = {
        nombre: nombre,
        descripcion: descripcion,
        usuario: { idUsuario: parseInt(usuarioId) } 
    };

    fetch("/api/materias", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(materiaObj)
    })
    .then(res => res.ok ? res.json() : Promise.reject("Error en la respuesta"))
    .then(() => {
        const modalElement = document.getElementById('modalMateria');
        const modalInstance = bootstrap.Modal.getInstance(modalElement);
        if (modalInstance) modalInstance.hide();

        Swal.fire({
            title: '¡Logrado!',
            text: 'Materia guardada con éxito',
            icon: 'success',
            timer: 1000,
            showConfirmButton: false
        }).then(() => location.reload());
    })
    .catch(err => {
        console.error(err);
        Swal.fire('Error', 'No se pudo guardar la materia.', 'error');
    });
}

/**
 * Borrado lógico/físico con confirmación de seguridad (DELETE)
 */
function eliminarMateria(idMateria, nombreMateria) {
    Swal.fire({
        title: `¿Eliminar ${nombreMateria}?`,
        text: "¡Esta acción no se puede deshacer y borrará todas sus tareas vinculadas!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ef4444', 
        cancelButtonColor: '#6b7280',
        confirmButtonText: 'Sí, eliminar todo',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/api/materias/${idMateria}`, {
                method: 'DELETE'
            })
            .then(res => {
                if (res.ok) {
                    Swal.fire({
                        title: '¡Borrado!',
                        text: 'La materia ha sido eliminada correctamente.',
                        icon: 'success',
                        timer: 1500,
                        showConfirmButton: false
                    }).then(() => location.reload());
                } else {
                    throw new Error("Error al eliminar");
                }
            })
            .catch(err => {
                console.error(err);
                Swal.fire('Error', 'No se pudo eliminar la materia. Revisa si tiene tareas activas.', 'error');
            });
        }
    })
}

function verTareas(id) {
    localStorage.setItem("materiaSeleccionada", id);
    window.location.href = "tareas.html";
}

function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}