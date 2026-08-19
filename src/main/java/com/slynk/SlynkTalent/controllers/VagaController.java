package com.slynk.SlynkTalent.controllers;

import com.slynk.SlynkTalent.models.Candidato;
import com.slynk.SlynkTalent.models.Vaga;
import com.slynk.SlynkTalent.repository.CandidatoRepository;
import com.slynk.SlynkTalent.repository.VagaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VagaController {

    private VagaRepository vr;
    private CandidatoRepository cr;

    public VagaController(VagaRepository vr, CandidatoRepository cr) {
        this.vr = vr;
        this.cr = cr;
    }

    @RequestMapping(value = "/cadastrarVaga", method = RequestMethod.GET)
    public String form() {
        return "vaga/formVaga";
    }

    @RequestMapping(value = "/cadastrarVaga", method = RequestMethod.POST)
    public String form(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/cadastrarVaga";
        }

        vr.save(vaga);
        attributes.addFlashAttribute("mensagem", "Vaga Cadastrada com sucesso!");
        return "redirect:/cadastrarVaga";
    }

    @RequestMapping("/vagas")
    public ModelAndView listaVagas() {
        ModelAndView mv = new ModelAndView("vaga/listaVagas");
        Iterable<Vaga> vagas = vr.findAll();
        mv.addObject("vagas", vagas);
        return mv;
    }

    @RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
    public ModelAndView detalhesVaga(@PathVariable("codigo") long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("vaga/detalhesVaga");
        mv.addObject("vaga", vaga);

        Iterable<Candidato>candidatos = cr.findByVaga(vaga);
        mv.addObject("candidatos", candidatos);

        return mv;
    }

    @RequestMapping("/deletarVaga")
    public String deletarVaga(long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        vr.delete(vaga);
        return "redirect:/vagas";
    }

    @RequestMapping(value = "/{codigo:\\d+}", method = RequestMethod.POST)
    public String detalhesVaga(@PathVariable("codigo") long codigo, @Valid Candidato candidato, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/" + codigo;
        }

        if (cr.findByRg(candidato.getRg()) != null) {
            attributes.addFlashAttribute("mensagemErro", "RG duplicado!");
            return "redirect:/" + codigo;
        }

        Vaga vaga = vr.findByCodigo(codigo);
        candidato.setVaga(vaga);
        cr.save(candidato);
        attributes.addFlashAttribute("mensagem", "Candidato Cadastrado com sucesso!");
        return "redirect:/" + codigo;
    }

    @RequestMapping("/deletarCandidato")
    public String deletarCandidato(String rg) {
        Candidato candidato = cr.findByRg(rg);
        Vaga vaga = candidato.getVaga();
        String codigo = "" + vaga.getCodigo();

        cr.delete(candidato);

        return "redirect:/" + codigo;
    }

    public ModelAndView editarVaga(Long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("vaga/update-Vaga");
        mv.addObject("vaga", vaga);
        return mv;
    }

    @RequestMapping(value = "/editar-vaga", method = RequestMethod.GET)
    public ModelAndView editarVaga(long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("vaga/update-Vaga");
        mv.addObject("vaga", vaga);
        return mv;
    }

    @RequestMapping(value = "/editar-vaga", method = RequestMethod.POST)
    public String updateVaga(@Valid Vaga vaga,  BindingResult result, RedirectAttributes attributes) {
        vr.save(vaga);
        attributes.addFlashAttribute("sucess", "Vaga alterada com sucesso!");

        long codigoLong = vaga.getCodigo();
        String codigo = "" + codigoLong;
        return "redirect:/" + codigo;
    }
}
