# Stock Price Monitor

A real-time stock price monitoring application built with **Java 24** to experiment with modern concurrency and stream processing.

## Learning Goals

- **Virtual Threads** - Master Java's lightweight concurrency for I/O-heavy operations
- **Stream Processing** - Transform and analyze financial data using Java streams
- **Moving Averages** - Calculate price trends over time windows
- **Momentum Analysis** - Detect rate of price changes and market direction

## Tech Stack

- **Java 24** with modern record classes
- **Spring Boot 3.3.x** for framework foundation
- **PostgreSQL** for data persistence (will be added later)
- **WebSocket** for real-time updates
- **Financial APIs** for live market data

## Project Structure

```
src/main/java/com/amraljundi/stockmonitor/
├── model/          # Data models (StockPrice, MovingAverage, Momentum)
├── service/        # Business logic (API clients, calculators)
├── streams/        # Stream processing pipelines
├── config/         # Virtual thread configuration
├── controller/     # REST endpoints
└── websocket/      # Real-time WebSocket handlers
```

## Data Models

- **Stock** - Represents monitored stock symbols (AAPL, GOOGL, etc.)
- **StockPrice** - Individual price points with timestamp (immutable record)
- **MovingAverage** - Calculated trend averages (immutable record)
- **Momentum** - Price change velocity analysis (immutable record)

## Development Progress

- [x] Project setup and data models
- [x] Virtual threads configuration
- [ ] API integration with concurrent fetching
- [ ] Stream processing pipeline
- [ ] Moving average calculations
- [ ] Momentum detection
- [ ] Database integration
- [ ] WebSocket real-time updates

## Key Concepts

**Moving Average**: Average price over N time periods, smooths out price fluctuations to show trends
**Momentum**: Rate of price change (current price - price N periods ago), indicates market direction

## Getting Started

1. Clone the repository
2. Ensure Java 24 is installed
3. Run `./mvnw spring-boot:run`
4. Follow the step-by-step learning modules

---

*This is a learning project focused on modern Java concurrency and stream processing techniques.*