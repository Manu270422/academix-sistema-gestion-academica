/**
 * js/recuperar.js
 * Maneja la comunicación real con el API de recuperación de contraseña
 * Academix Pro - Seguridad Nivel 1 (Producción)
 */

async function solicitarRecuperacion() {
    const emailInput = document.getElementById("emailRecuperar");
    const email = emailInput.value.trim();
    const btnRecuperar = document.querySelector("button[onclick='solicitarRecuperacion()']");

    // 1. Validación básica de entrada
    if (!email) {
        Swal.fire({
            title: '¡Ojo!',
            text: 'Por favor, ingresa tu correo electrónico o usuario.',
            icon: 'warning',
            confirmButtonColor: '#4338ca'
        });
        return;
    }

    // 2. Validación flexible
    const esEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    const esUsuario = email.length >= 3;

    if (!esEmail && !esUsuario) {
        Swal.fire({
            title: 'Formato inválido',
            text: 'Asegúrate de escribir un correo válido o un nombre de usuario registrado.',
            icon: 'error',
            confirmButtonColor: '#4338ca'
        });
        return;
    }

    // 3. Efecto de carga (Bloqueo de UI)
    btnRecuperar.disabled = true;
    const textoOriginal = btnRecuperar.innerHTML;
    btnRecuperar.innerHTML = `
        <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
        ENVIANDO SOLICITUD...
    `;

    // 4. CONEXIÓN REAL CON EL BACKEND (FETCH)
    try {
        const respuesta = await fetch('/api/auth/recuperar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: email }) // Enviamos el dato al Controller
        });

        if (respuesta.ok) {
            // Transición exitosa de interfaz
            const formulario = document.getElementById("formularioRecuperacion");
            const mensajeExito = document.getElementById("mensajeExito");

            if (formulario && mensajeExito) {
                formulario.classList.add("d-none");
                mensajeExito.classList.remove("d-none");
                console.log(`✅ Servidor procesó la solicitud para: ${email}`);
            }
        } else {
            // Si el servidor responde con error (500, 404, etc)
            throw new Error("Error en la respuesta del servidor");
        }

    } catch (error) {
        console.error("Error en la petición:", error);
        btnRecuperar.disabled = false;
        btnRecuperar.innerHTML = textoOriginal;
        
        Swal.fire({
            title: 'Error de conexión',
            text: 'No pudimos contactar con el servidor. Revisa tu conexión a internet o intenta más tarde.',
            icon: 'error',
            confirmButtonColor: '#4338ca'
        });
    }
}

// 5. Soporte para la tecla "Enter"
document.getElementById("emailRecuperar")?.addEventListener("keypress", (e) => {
    if (e.key === "Enter") {
        solicitarRecuperacion();
    }
});