package br.com.washii.presentation.perfil;

import br.com.washii.service.UsuarioService;

public class PerfilClienteController {
    
    private UsuarioService usuarioService;

    public PerfilClienteController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
}
