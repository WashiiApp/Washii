package br.com.washii.persistence;

import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.entities.Servico;
import br.com.washii.domain.enums.CategoriaServico;
import br.com.washii.domain.repository.ServicoRepository;

import java.util.List;

public class teste {

    public static void main(String[] args) {

        ServicoRepository servicoRepo = new ServicoPersistence();


//        Negocio negocio = new Negocio() {};
//        negocio.setId(5L);
//
//
//        Servico servico = new Servico();
//        servico.setId(1L);
//        servico.setNome("Lavagem simples");
//        servico.setDescricao("Lavagem simples de washii");
//        servico.setCategoriaServico(CategoriaServico.POLIMENTO);
//        servico.setPrecoBase(80.0);
//        servico.setNegocio(negocio);
//
//        servicoRepo.atualizar(servico);
//
//
//        servicoRepo.salvar(servico);
//        System.out.println("Serviço salvo com sucesso!");


//        servicoRepo.buscarPorId(1L);
//        System.out.println("save");

        System.out.println(servicoRepo.buscarPorId(5L));

//
//      List<Servico> servicos = servicoRepo.listarTodos();
//
////
//        for (Servico s : servicos) {
//            System.out.println(s.getId() + " - " + s.getNome() + " - R$ " + s.getPrecoBase());
        }
    }


