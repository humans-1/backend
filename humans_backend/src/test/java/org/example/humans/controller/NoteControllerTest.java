package org.example.humans.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.humans.dto.NoteDto;
import org.example.humans.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class NoteControllerTest {

    @InjectMocks
    private NoteController noteController;

    @Mock
    private NoteService noteService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void createSubjectTest() throws Exception {
        NoteDto requestDto = new NoteDto("테스트 과목", 1L);
        NoteDto responseDto = new NoteDto("테스트 과목", 1L);

        when(noteService.createSubject(any(Long.class), any(NoteDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/1/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subject").value("테스트 과목"))
                .andExpect(jsonPath("$.noteId").value(1));

        verify(noteService, times(1)).createSubject(eq(1L), any(NoteDto.class));
    }

    @Test
    public void getAllSubjectsTest() throws Exception {
        NoteDto noteDto = new NoteDto("테스트 과목", 1L);
        List<NoteDto> notes = Collections.singletonList(noteDto);

        when(noteService.findAllSubject(any(Long.class))).thenReturn(notes);

        mockMvc.perform(get("/1/note"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subject").value("테스트 과목"))
                .andExpect(jsonPath("$[0].noteId").value(1));

        verify(noteService, times(1)).findAllSubject(eq(1L));
    }

    @Test
    public void deleteNoteTest() throws Exception {
        mockMvc.perform(delete("/1/note?subject=테스트 과목"))
                .andExpect(status().isNoContent());

        verify(noteService, times(1)).deleteNote(1L, "테스트 과목"); // ID와 과목명 모두 전달
    }




}
