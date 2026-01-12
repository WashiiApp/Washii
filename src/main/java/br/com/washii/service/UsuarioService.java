package br.com.washii.service;
import br.com.washii.domain.entities.Endereco;
import br.com.washii.domain.entities.LavaJato;
import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.entities.Usuario;
import br.com.washii.domain.enums.CategoriaServico;
import br.com.washii.domain.enums.CategoriaVeiculo;
import br.com.washii.domain.enums.TipoUsuario;
import br.com.washii.domain.exceptions.EmailJaCadastradoException;
import br.com.washii.domain.repository.UsuarioRepository;
import br.com.washii.infra.security.SenhaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Camada de Serviço responsável pelas regras de negócio do Usuário
 */
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Injeção do Repository pelo construtor
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Salva um novo usuário no sistema
     */

    public void salvarNovoUsuario(Usuario user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        Optional<Usuario>usuario = usuarioRepository.buscarPorEmail(user.getEmail());
        if (usuario.isPresent()){
            throw new EmailJaCadastradoException();
        }

        String  SenhaCrua = user.getSenha();
        String SenhaCripto = SenhaUtils.hashSenha(SenhaCrua);
        user.setSenha(SenhaCripto);

        usuarioRepository.salvar(user);
    }

    /**
     * Atualiza os dados de um usuário existente
     */
    public void atualizarUsuario(Usuario user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Usuário inválido para atualização");
        }

        usuarioRepository.atualizar(user);
    }

    /**
     * Remove um usuário do sistema
     */
    public void removerUsuario(Usuario user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Usuário inválido para remoção");
        }

        usuarioRepository.remover(user.getId());
    }

    public List<Negocio> listarTodosNegocios(){
        return usuarioRepository.listarTodosLavaJatos();
        //addNegocios();
        //return negocios;
    }

    private List<Negocio> negocios= new ArrayList<>();

    private void addNegocios(){
        for(int i = 0; i < 5; i++){
            Endereco end = new Endereco();
            end.setRua("Ruas dos Cavalos");
            end.setEstado("Paraíba");
            end.setCidade("Feijão");
            LavaJato lj = new LavaJato("Washii", "washii@gmail.com", "234", end, TipoUsuario.NEGOCIO);

            lj.addCategoriasAceitas(CategoriaVeiculo.CARRO);
            lj.addCategoriasAceitas(CategoriaVeiculo.MOTO);

            Servico ser= new Servico("Lavagem simples", "Lavagem com água e sabão", CategoriaServico.LAVAGEM_SIMPLES, 65, lj);
            lj.addServicosOferecidos(ser);

            negocios.add(lj);
        }
    }
}
