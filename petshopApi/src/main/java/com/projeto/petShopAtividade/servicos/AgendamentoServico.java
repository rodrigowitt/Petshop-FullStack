package com.projeto.petShopAtividade.servicos;

import com.projeto.petShopAtividade.modelos.AgendamentoModelo;
import com.projeto.petShopAtividade.modelos.ClientePetshopModelo;
import com.projeto.petShopAtividade.repositorios.AgendamentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoServico {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    final AgendamentoRepositorio agendamentoRepositorio;

    public AgendamentoServico(AgendamentoRepositorio agendamentoRepositorio) {
        this.agendamentoRepositorio = agendamentoRepositorio;
    }

    public AgendamentoModelo save(AgendamentoModelo agendamentoModelo) {
        return agendamentoRepositorio.save(agendamentoModelo);
    }

    public List<AgendamentoModelo> findAll() {
        return agendamentoRepositorio.findAll();
    }

    public Optional<AgendamentoModelo> findById(Integer id) {
        return agendamentoRepositorio.findById(id);
    }

    public void delete(AgendamentoModelo agendamentoModelo) {
        agendamentoRepositorio.delete(agendamentoModelo);
    }
    public List<AgendamentoModelo> agendamentoMaisRecentes() {
        String sql = "SELECT *\n" +
                "FROM tb_agendamento\n" +
                "WHERE horario::time >= CURRENT_TIME AND to_date(data, 'YYYY/MM/DD') = CURRENT_DATE\n"+
                "ORDER BY data || ' ' || horario ASC";


        List <AgendamentoModelo>  resultado= jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AgendamentoModelo.class));
        return resultado;
    }
}
