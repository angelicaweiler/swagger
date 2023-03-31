package com.example.swagger.business;

import com.example.swagger.infrastructure.entities.Pessoa;
import com.example.swagger.infrastructure.entities.Profissao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.swagger.infrastructure.entities.Profissao.DEVELOPER;
import static org.springframework.util.Assert.notNull;

@Service
@Component
@Slf4j
public class SwaggerService {

    @Value("${path.documents}")
    private String path;

    public ResponseEntity<String> uploadDocument(MultipartFile file) {
        try{
            notNull(file, "arquivo é obrigatório!");

            String rootFile = path + "/" + file.getOriginalFilename();
            File newDocument = new File(rootFile);
            FileOutputStream fileOutputStream = new FileOutputStream(newDocument, true);

            file.getInputStream().transferTo(fileOutputStream);
            return ResponseEntity.ok("Arquivo carregado " + file.getName());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar arquivo");
        }
    }

    public List<Pessoa> buscaPessoasPor(Profissao profissao, Integer idade) {
        List<Pessoa> pessoas = buscarPessoas();

        return pessoas.stream()
                .filter(Objects::nonNull)
                .filter(pessoa -> pessoa.profissao().equals(profissao))
                .filter(pessoa -> Objects.equals(pessoa.idade(), idade))
                .collect(Collectors.toList());
    }
    private static List<Pessoa> buscarPessoas() {

        return Arrays.asList(
                new Pessoa("Angelica", 30, BigDecimal.TEN, DEVELOPER),
                new Pessoa("Maria", 37, BigDecimal.TEN, DEVELOPER),
                new Pessoa("Pedro", 40, BigDecimal.ONE, Profissao.SCRUM_MASTER),
                new Pessoa("Felipe", 28, BigDecimal.ONE, Profissao.SCRUM_MASTER),
                new Pessoa("Joao", 43, BigDecimal.ONE, Profissao.PRODUCT_OWNER),
                new Pessoa("Elder", 46, BigDecimal.ONE, Profissao.PRODUCT_OWNER)
        );
    }



















}
