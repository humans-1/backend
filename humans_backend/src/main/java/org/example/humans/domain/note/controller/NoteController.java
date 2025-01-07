package org.example.humans.domain.note.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.humans.domain.note.converter.NoteConverter;
import org.example.humans.domain.note.dto.NoteReqDTO;
import org.example.humans.domain.note.dto.NoteResDTO;
import org.example.humans.domain.note.entity.Note;
import org.example.humans.domain.note.service.NoteCommandService;
import org.example.humans.domain.note.service.NoteQueryService;
import org.example.humans.global.apiPayload.CustomResponse;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
@Tag(name = "과목 API")
public class NoteController {
    private final NoteCommandService noteCommandService;
    private final NoteQueryService noteQueryService;

    @PostMapping("")
    @Operation(method = "POST", summary = "과목 생성 API", description = "과목 생성하는 API")
    public CustomResponse<NoteResDTO.NoteDetailsDTO> createArticle(@RequestBody NoteReqDTO.CreateNoteDTO dto) {
        NoteResDTO.NoteDetailsDTO responseDto = noteCommandService.createNote(dto);
        return CustomResponse.onSuccess(HttpStatus.CREATED, responseDto);
    }

    @GetMapping("/{noteId}")
    @Operation(method = "GET", summary = "단일 과목 조회 API", description = "단일 과목 조회하는 API")
    public CustomResponse<NoteResDTO.NoteDetailsDTO> getArticle(@PathVariable("noteId") Long noteId) {
        NoteResDTO.NoteDetailsDTO responseDto = noteQueryService.getNote(noteId);
        return CustomResponse.onSuccess(HttpStatus.OK,responseDto);
    }

    @GetMapping("")
    @Operation(method = "GET", summary = "커서 기반 페이지네이션 API", description = "게시글 커서 기반 페이지네이션 API")
    @Parameters({
            @Parameter(name = "cursor", description = "커서 값, 처음이면 0"),
    })
    public CustomResponse<NoteResDTO.NotePagePreviewDTO> getArticlesByCursor(
            @RequestParam("cursor") Long cursor,
            @RequestParam(value = "offset", defaultValue = "8") Integer offset
    ){
        Slice<Note> notes = noteQueryService.getNotesByCursor(cursor, offset);
        NoteResDTO.NotePagePreviewDTO result = NoteConverter.toNotePageDTO(notes);
        return CustomResponse.onSuccess(HttpStatus.OK,result);
    }

    @DeleteMapping("/{noteId}")
    @Operation(method = "DELETE", summary = "게시글 삭제 API", description = "게시글 삭제하는 API")
    public CustomResponse<String> deleteArticle(@PathVariable("noteId") Long noteId){
        noteCommandService.deleteNote(noteId);
        return CustomResponse.onSuccess(HttpStatus.NO_CONTENT,"해당 게시글이 삭제되었습니다.");
    }
}
