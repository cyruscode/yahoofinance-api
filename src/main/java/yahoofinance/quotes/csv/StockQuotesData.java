package yahoofinance.quotes.csv;

import yahoofinance.Stock;
import yahoofinance.Utils;
import yahoofinance.exchanges.ExchangeTimeZone;
import yahoofinance.quotes.stock.StockDividend;
import yahoofinance.quotes.stock.StockQuote;
import yahoofinance.quotes.stock.StockStats;

import static yahoofinance.quotes.csv.QuotesProperty.*;
import static yahoofinance.quotes.csv.QuotesProperty.MarketCapitalization;

/**
 *
 * @author Stijn Strickx
 */
public class StockQuotesData {
    
    private final String[] data;
    
    public StockQuotesData(String[] data) {
        this.data = data;
    }
    
    public String getValue(QuotesProperty property) {
        int propertyIndex = StockQuotesRequest.DEFAULT_PROPERTIES.indexOf(property);
        if(propertyIndex >= 0 && propertyIndex < this.data.length) {
            return this.data[propertyIndex];
        }
        return null;
    }
    
    public StockQuote getQuote() {
        String symbol = this.getValue(Symbol);
        StockQuote quote = new StockQuote(symbol);
        
        quote.setPrice(Utils.getBigDecimal(this.getValue(LastTradePriceOnly)));
        quote.setLastTradeSize(Utils.getLong(this.getValue(LastTradeSize)));
        quote.setAsk(Utils.getBigDecimal(this.getValue(AskRealtime), this.getValue(Ask)));
        quote.setAskSize(Utils.getLong(this.getValue(AskSize)));
        quote.setBid(Utils.getBigDecimal(this.getValue(BidRealtime), this.getValue(Bid)));
        quote.setBidSize(Utils.getLong(this.getValue(BidSize)));
        quote.setOpen(Utils.getBigDecimal(this.getValue(Open)));
        quote.setPreviousClose(Utils.getBigDecimal(this.getValue(PreviousClose)));
        quote.setDayHigh(Utils.getBigDecimal(this.getValue(DaysHigh)));
        quote.setDayLow(Utils.getBigDecimal(this.getValue(DaysLow)));
        
        quote.setTimeZone(ExchangeTimeZone.getStockTimeZone(symbol));
        quote.setLastTradeDateStr(this.getValue(LastTradeDate));
        quote.setLastTradeTimeStr(this.getValue(LastTradeTime));
        quote.setLastTradeTime(Utils.parseDateTime(this.getValue(LastTradeDate), this.getValue(LastTradeTime), quote.getTimeZone()));
        
        quote.setYearHigh(Utils.getBigDecimal(this.getValue(YearHigh)));
        quote.setYearLow(Utils.getBigDecimal(this.getValue(YearLow)));
        quote.setPriceAvg50(Utils.getBigDecimal(this.getValue(FiftydayMovingAverage)));
        quote.setPriceAvg200(Utils.getBigDecimal(this.getValue(TwoHundreddayMovingAverage)));
        
        quote.setVolume(Utils.getLong(this.getValue(Volume)));
        quote.setAvgVolume(Utils.getLong(this.getValue(AverageDailyVolume)));
        
        return quote;
    }
    
    public StockStats getStats() {
        String symbol = this.getValue(Symbol);
        StockStats stats = new StockStats(symbol);
        
        stats.setMarketCap(Utils.getBigDecimal(this.getValue(MarketCapitalization)));
        stats.setSharesFloat(Utils.getLong(this.getValue(SharesFloat)));
        stats.setSharesOutstanding(Utils.getLong(this.getValue(SharesOutstanding)));
        stats.setSharesOwned(Utils.getLong(this.getValue(SharesOwned)));
        
        stats.setEps(Utils.getBigDecimal(this.getValue(DilutedEPS)));
        stats.setPe(Utils.getBigDecimal(this.getValue(PERatio)));
        stats.setPeg(Utils.getBigDecimal(this.getValue(PEGRatio)));
        
        stats.setEpsEstimateCurrentYear(Utils.getBigDecimal(this.getValue(EPSEstimateCurrentYear)));
        stats.setEpsEstimateNextQuarter(Utils.getBigDecimal(this.getValue(EPSEstimateNextQuarter)));
        stats.setEpsEstimateNextYear(Utils.getBigDecimal(this.getValue(EPSEstimateNextYear)));
        
        stats.setPriceBook(Utils.getBigDecimal(this.getValue(PriceBook)));
        stats.setPriceSales(Utils.getBigDecimal(this.getValue(PriceSales)));
        stats.setBookValuePerShare(Utils.getBigDecimal(this.getValue(BookValuePerShare)));
        
        stats.setOneYearTargetPrice(Utils.getBigDecimal(this.getValue(OneyrTargetPrice)));
        stats.setEBITDA(Utils.getBigDecimal(this.getValue(EBITDA)));
        stats.setRevenue(Utils.getBigDecimal(this.getValue(Revenue)));
        
        stats.setShortRatio(Utils.getBigDecimal(this.getValue(ShortRatio)));
        
        return stats;
    }
    
    public StockDividend getDividend() {
        String symbol = this.getValue(Symbol);
        StockDividend dividend = new StockDividend(symbol);
        
        dividend.setPayDate(Utils.parseDividendDate(this.getValue(DividendPayDate)));
        dividend.setExDate(Utils.parseDividendDate(this.getValue(ExDividendDate)));
        dividend.setAnnualYield(Utils.getBigDecimal(this.getValue(TrailingAnnualDividendYield)));
        dividend.setAnnualYieldPercent(Utils.getBigDecimal(this.getValue(TrailingAnnualDividendYieldInPercent)));
        
        return dividend;
    }
    
    public Stock getStock() {
        String symbol = this.getValue(Symbol);
        Stock stock = new Stock(symbol);
        
        stock.setName(Utils.getString(this.getValue(Name)));
        stock.setCurrency(Utils.getString(this.getValue(Currency)));
        stock.setStockExchange(Utils.getString(this.getValue(StockExchange)));
        
        stock.setQuote(this.getQuote());
        stock.setStats(this.getStats());
        stock.setDividend(this.getDividend());
        
        return stock;
    }
    
}
