package tools.dbconnector8.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class QueryResultModel {
	private boolean withResultSet;
	private int count;
	private LocalDateTime executeStart;
	private LocalDateTime executeEnd;
}
