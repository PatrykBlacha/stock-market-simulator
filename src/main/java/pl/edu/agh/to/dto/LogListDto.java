package pl.edu.agh.to.dto;

import lombok.Data;

import java.util.List;

@Data
public class LogListDto {
    private List<LogEntryDto> log;
}
