package br.com.souza.twitterclone.accounts.dto.pagination;

import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class CustomPage<T>{

    private List<T> content;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalItems;

    public CustomPage() {
    }

    public CustomPage(Page<T> page) {
        this.content = page.getContent();
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
    }
}
