package br.com.tizo.controllers;

import br.com.tizo.data.vo.v1.BookVO;
import br.com.tizo.data.vo.v1.PersonVO;
import br.com.tizo.services.BookServices;
import br.com.tizo.util.MediaTypeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Books", description = "Endpoints fot Managing Books")
public class BookController {

    @Autowired
    BookServices bookServices;

    @GetMapping(produces = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML})
    @Operation(
            summary = "Finds all Books",
            description = "Finds all Books",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = PersonVO.class)
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public List<BookVO> findAll(){
        return bookServices.findAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML})
    @Operation(
            summary = "Finds a Book",
            description = "Finds a Book",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonVO.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public BookVO findById(@PathVariable(value = "id") Integer id) {

        return bookServices.findById(id);
    }

    @PostMapping(
            produces = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML},
            consumes = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML})
    @Operation(
            summary = "Adds a new Book",
            description = "Adds a new Book by passing in a JSON, XML or YMl representation of the book",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonVO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public BookVO create(@RequestBody BookVO book) {
        return bookServices.create(book);
    }

    @PutMapping(
            produces = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML},
            consumes = {MediaTypeUtil.APPLICATION_JSON, MediaTypeUtil.APPLICATION_XML, MediaTypeUtil.APPLICATION_YAML})
    @Operation(
            summary = "Updates a Book",
            description = "Updates a Book by passing in a JSON, XML or YMl representation of the book",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Updated",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonVO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public BookVO update(@RequestBody BookVO book) {
        return bookServices.update(book);
    }




    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete a Book",
            description = "Delete a Book by passing in a JSON, XML or YMl representation of the book",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer id) {
        bookServices.delete(id);
        return ResponseEntity.noContent().build();
    }
}

