package com.digital_library_management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class BookDto {
        private Long id;
        @NotBlank(message = "Title is required")
        private String title;
        @NotBlank(message = "Author is required")
        private String author;
        @NotBlank(message = "ISBN is required")
        private String isbn;
        @NotBlank(message = "Category is required")
        private String category;
        private String publisher;
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantityTotal;
        private String rackLocation;
        private String description;
}

