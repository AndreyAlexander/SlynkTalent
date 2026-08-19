package com.slynk.SlynkTalent.repository;

import com.slynk.SlynkTalent.models.Candidato;
import com.slynk.SlynkTalent.models.Vaga;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CandidatoRepository extends CrudRepository<Candidato, Long> {

    Iterable<Candidato> findByVaga(Vaga vaga);

    Candidato findByRg(String rg);

    List<Candidato> findAllByNomeCandidato(String nomeCandidato);
}
