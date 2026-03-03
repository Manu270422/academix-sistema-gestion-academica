function confirmarCambio() {
    const p1 = document.getElementById("pass1").value;
    const p2 = document.getElementById("pass2").value;
    
    // Obtener el token de la URL (ej: ?token=ABC123XYZ)
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    if (p1.length < 8) {
        Swal.fire('Error', 'La contraseña debe tener al menos 8 caracteres.', 'error');
        return;
    }

    if (p1 !== p2) {
        Swal.fire('Error', 'Las contraseñas no coinciden.', 'error');
        return;
    }

    // Aquí llamarías a tu nuevo endpoint en el AuthController
    fetch('/api/auth/actualizar-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ token: token, nuevaPassword: p1 })
    })
    .then(res => {
        if (res.ok) {
            document.getElementById("formRestablecer").classList.add("d-none");
            document.getElementById("mensajeFinal").classList.remove("d-none");
        } else {
            Swal.fire('Error', 'El enlace ha expirado o es inválido.', 'error');
        }
    });
}