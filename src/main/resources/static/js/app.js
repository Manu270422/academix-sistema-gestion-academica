function login() {
    console.log("Intentando iniciar sesión..."); 
    
    // 1. Obtenemos los elementos del DOM
    const correoInput = document.getElementById("correo");
    const passwordInput = document.getElementById("password");

    if (!correoInput || !passwordInput) {
        console.error("No se encontraron los inputs en el HTML");
        return;
    }

    const valorCorreo = correoInput.value;
    const valorPassword = passwordInput.value;

    // 2. Validación de campos vacíos
    if(!valorCorreo || !valorPassword) {
        Swal.fire({
            title: 'Campos vacíos',
            text: 'Por favor llena todos los datos',
            icon: 'warning',
            confirmButtonColor: '#6366f1'
        });
        return;
    }

    // 3. Envío al servidor (Sincronizado con UsuarioController y Usuario.java)
    fetch("/api/usuarios/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        // IMPORTANTE: Aquí cambiamos los nombres de las llaves
        body: JSON.stringify({ 
            email: valorCorreo,      // Antes era 'correo'
            password: valorPassword  // Antes era 'contraseña'
        })
    })
    .then(res => {
        if (res.ok) return res.json();
        throw new Error("Credenciales incorrectas");
    })
    .then(data => {
        // Guardamos los datos actualizados
        localStorage.setItem("usuarioId", data.idUsuario);
        localStorage.setItem("usuarioNombre", data.nombre);
        
        Swal.fire({
            title: `¡Hola ${data.nombre}!`,
            text: 'Acceso concedido',
            icon: 'success',
            timer: 1500,
            showConfirmButton: false
        }).then(() => {
            window.location.href = "dashboard.html";
        });
    })
    .catch(error => {
        console.error("Error en login:", error);
        Swal.fire({
            title: 'Error de acceso',
            text: 'Correo o contraseña incorrectos',
            icon: 'error',
            confirmButtonColor: '#6366f1'
        });
    });
}