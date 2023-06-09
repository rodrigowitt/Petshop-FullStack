package com.projeto.petShopAtividade.controle;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.projeto.petShopAtividade.dto.ClientePetshopDto;
import com.projeto.petShopAtividade.dto.PetshopDto;
import com.projeto.petShopAtividade.enums.StatusTratamento;
import com.projeto.petShopAtividade.modelos.ClientePetshopModelo;
import com.projeto.petShopAtividade.modelos.PetshopModelo;
import com.projeto.petShopAtividade.servicos.ClientePetshopServico;
import com.projeto.petShopAtividade.servicos.PetshopServico;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/petshop")
public class PetshopControle {

    final PetshopServico petshopServico;


    @Autowired
    ClientePetshopServico clientePetshopServico;


    private JavaMailSender mailSender;

    public PetshopControle(PetshopServico petshopServico, JavaMailSender mailSender) {
        this.petshopServico = petshopServico;
        this.mailSender = mailSender;
    }

    @PostMapping
    public ResponseEntity<Object> savePetshop(
            @RequestBody
            @Valid
            PetshopDto petshopDto , ClientePetshopDto clientePetshopDto){
        Optional<ClientePetshopModelo> clienteModelo = clientePetshopServico.findById(petshopDto.getClienteid());
        var petshopModelo = new PetshopModelo();
        petshopDto.setResponsavel(clienteModelo.get().getNome());

        BeanUtils.copyProperties(petshopDto, petshopModelo);

        petshopModelo.setEntrada(LocalDateTime.from(LocalDateTime.now()));
        petshopModelo.setStatusTratamento(String.valueOf(StatusTratamento.PREPARANDO));
        petshopModelo.setResponsavel(clienteModelo.get().getNome());
        petshopModelo.setClientePetshopModelo(clienteModelo.get());
        System.out.println("O contato é: " + clienteModelo.get().getTelefone());
        petshopModelo.setContato(clienteModelo.get().getTelefone());

        String entradaMail = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm:ss"));
        System.out.println(entradaMail);



        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("Olá "+clienteModelo.get().getNome()+ ", o seu pet "+ petshopDto.getNome()+" foi cadastrado as "+ entradaMail + " Obrigado!");
        message.setTo(clienteModelo.get().getEmail());
        message.setFrom("rodrigowitthoeft95@gmail.com");
        message.setSubject("Petshop");

        try {
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(petshopServico.save(petshopModelo));
    }

    @GetMapping(value = "/relatorios/{dias}/{status}")
    public ResponseEntity<List<PetshopModelo>> getDataPetshop(@PathVariable(value = "dias")String dias,@PathVariable(value = "status") String status){
        System.out.println("Ok");

        return ResponseEntity.status(HttpStatus.OK).body(petshopServico.ultimosDias(dias,status));
    }

    @GetMapping(value = "/recentes")
    public ResponseEntity<List<PetshopModelo>> getRecentesPetshop(){
        System.out.println("Executando mais recentes");

        return ResponseEntity.status(HttpStatus.OK).body(petshopServico.maisRecentes());
    }

    @GetMapping(value = "/lucro")
    public ResponseEntity<Double> getLucroPetshop(){
        System.out.println("Executando mais recentes");

        return ResponseEntity.status(HttpStatus.OK).body(petshopServico.calcularSoma());
    }



    @GetMapping
    public ResponseEntity<List<PetshopModelo>> getAllPetshop() {
        System.out.println("Executando o total do mês");
        System.out.println(petshopServico.calcularSoma());
        return ResponseEntity.status(HttpStatus.OK).body(petshopServico.findAll());
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getIdPetshop(@PathVariable(value = "id") UUID id) {
        Optional<PetshopModelo> petshopModeloOptional = petshopServico.findById(id);

        if (!petshopModeloOptional.isPresent()) {
            //Retorna com o status Ok e um Json vazio para evitar erros no javascript.
            return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.OK).body(petshopModeloOptional.get());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePetshop(@PathVariable(value = "id") UUID id) {
        Optional<PetshopModelo> petshopModeloOptional = petshopServico.findById(id);
        if (!petshopModeloOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado");
        } else {
            petshopServico.delete(petshopModeloOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
    @JsonSerialize
    public class EmptyJsonResponse { }
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizaPetshop(@PathVariable(value = "id") UUID id, @RequestBody @Valid PetshopDto petshopDto,ClientePetshopDto clientePetshopDto ) {

        Optional<PetshopModelo> petshopModeloOptional = petshopServico.findById(id);


        if (!petshopModeloOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado");
        }

        var petshopModelo = petshopModeloOptional.get();


        BeanUtils.copyProperties(petshopDto, petshopModelo);

        petshopModelo.setId(petshopModeloOptional.get().getId());



        if(!petshopModelo.getStatusTratamento().equals(("PREPARANDO"))) {
            System.out.println("Endereço de email: "+ clientePetshopDto.getEmail());
            SimpleMailMessage message = new SimpleMailMessage();
            message.setText("Olá " + petshopModelo.getClientePetshopModelo().getNome() + ", houve uma atualização no tratamento "+petshopModelo.getTratamento() +" para " + petshopModelo.getStatusTratamento() + " do seu pet " + petshopDto.getNome() + "!");
            message.setTo(petshopModelo.getClientePetshopModelo().getEmail());
            message.setFrom("rodrigowitthoeft95@gmail.com");
            message.setSubject("Petshop");
            mailSender.send(message);
        }


            return ResponseEntity.status(HttpStatus.OK).body(petshopServico.save(petshopModelo));

    }

    @GetMapping(value = "download/{id}")
    public ResponseEntity<Object>getPdf(@PathVariable(value = "id") UUID id) throws IOException, MessagingException {
        Optional<PetshopModelo> petshopModeloOptional = petshopServico.findById(id);

        if (!petshopModeloOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado");
        }
        petshopServico.gerarPdf(petshopModeloOptional.get().getNome(),petshopModeloOptional.get().getEspecie(),petshopModeloOptional.get().getRaca(),
                petshopModeloOptional.get().getPeso(),petshopModeloOptional.get().getTratamento(),petshopModeloOptional.get().getClientePetshopModelo().getTelefone(),
                petshopModeloOptional.get().getClientePetshopModelo().getNome(),petshopModeloOptional.get().getValor());

        return new ResponseEntity(HttpStatus.OK);
    }
}
