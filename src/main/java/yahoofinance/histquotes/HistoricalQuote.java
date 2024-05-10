
package yahoofinance.histquotes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * All getters can return null in case the data is not available from Yahoo Finance.
 * 
 * @author Stijn Strickx
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class HistoricalQuote {
    
    private String symbol;
    
    private Calendar date;
    
    private BigDecimal open;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal close;
    
    private BigDecimal adjClose;
    
    private Long volume;
    
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(this.date.getTime());
        return this.symbol + "@" + dateStr + ": " + this.low + "-" + this.high + ", " + 
                this.open + "->" + this.close + " (" + this.adjClose + ")";
    }
}
