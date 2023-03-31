package com.example.swagger.api;

import com.example.swagger.business.SwaggerService;
import com.example.swagger.infrastructure.entities.Pessoa;
import com.example.swagger.infrastructure.entities.Profissao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class SwaggerControllerTest {

    @InjectMocks
    SwaggerController controller;

    @Mock
    private SwaggerService service;

    MockMvc mockMvc;

    private Pessoa pessoa;
    private Integer idade;
    private Profissao profissao;
    private MockMultipartFile file;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                        .alwaysDo(print())
                .build();
        file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

    pessoa = new Pessoa("João", 55, new BigDecimal(10), Profissao.QUALITY_ASSURANCE);
    idade = 55;
    profissao = Profissao.QUALITY_ASSURANCE;
    }

    @Test
    public void deveAceitarARequisicaoEChamarAServiceUploadDocumentosComSucesso() throws Exception {
        when(service.uploadDocument(file)).thenReturn(ResponseEntity.ok("Documento Carregado Com Sucesso!"));

        mockMvc.perform(multipart("/teste-open-api")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(service).uploadDocument(file);
        verifyNoMoreInteractions(service);

    }


    @Test
    public void deveBuscarOsDadosDaPessoaDeAcordoCOmIdadeEPRofissaoComSucesso() throws Exception {
        when(service.buscaPessoasPor(profissao, idade)).thenReturn(Collections.singletonList(pessoa));

        mockMvc.perform(get("/teste-open-api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("Profissão", profissao.toString())
                        .param("Idade", idade.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(service).buscaPessoasPor(profissao, idade);
        verifyNoMoreInteractions(service);

    }

    @Test
    public void deveRetonrarErroxxCasoNaoPassadosPArametrosObrigatorios() throws Exception {

        mockMvc.perform(get("/teste-open-api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("Idade", idade.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        verifyNoInteractions(service);

    }
}
