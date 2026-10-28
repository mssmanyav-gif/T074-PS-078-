# T074-PS-078-
# PS-078: Prepaid Recharge, Pack Stacking and Validity Engine

## Overview

The Prepaid Recharge, Pack Stacking and Validity Engine is a backend project that simulates the core operations of a prepaid telecommunications system.

The engine manages subscriber registration, recharges, pack validity, usage events, quota deduction, billing and subscriber lifecycle transitions. It applies predefined business rules to determine which pack serves a usage event, how much quota or core balance is deducted, and how each transaction is recorded.

The project focuses on implementing deterministic business logic, handling edge cases and maintaining an auditable transaction ledger.

## Objectives

- Register subscribers and maintain their account details.
- Process core-balance recharges and prepaid pack purchases.
- Calculate pack validity and handle pack stacking.
- Process voice, data and SMS usage events.
- Implement data quota deduction, rollover and fair usage policy (FUP).
- Handle roaming charges and loyalty bonuses.
- Manage subscriber lifecycle transitions.
- Generate usage and transaction ledgers.
- Ensure correct billing and balance handling in edge cases.

## Technology Stack

- **Language:** Java 21
- **Backend Framework:** Spring Boot
- **Build Tool:** Maven
- **Version Control:** Git
- **Testing:** JUnit 5
- **Initial Storage:** In-memory Java collections
- **Planned Database:** PostgreSQL

## Core Business Logic

The engine will implement the following business rules:

1. Pack validity calculation and stacking.
2. Data add-on consumption precedence.
3. Unlimited-pack daily data quotas.
4. Data rollover and bucket limits.
5. Post-quota data handling and FUP.
6. Voice and SMS billing.
7. National roaming and roaming add-ons.
8. Core-balance validity and renewal.
9. Subscriber lifecycle management.
10. Insufficient-balance rejection.
11. Loyalty bonuses for consecutive unlimited packs.
12. Billing events that cross midnight.

## Development Roadmap

Development will proceed incrementally.

- [ ] Create the Java 21 and Spring Boot project skeleton.
- [ ] Implement subscriber registration.
- [ ] Implement core-balance recharge and validity renewal.
- [ ] Implement unlimited-pack purchase and validity calculation.
- [ ] Implement pack stacking and queue management.
- [ ] Implement voice, data and SMS usage processing.
- [ ] Implement quota deduction, rollover and FUP.
- [ ] Implement roaming and loyalty bonuses.
- [ ] Implement subscriber lifecycle transitions.
- [ ] Generate date-range usage and transaction ledgers.
- [ ] Add automated tests for business rules and edge cases.
- [ ] Introduce persistent database storage.

## Project Structure

```text
prepaid-recharge-engine/
├── src/
│   ├── main/
│   │   └── java/com/capstone/recharge/
│   └── test/
│       └── java/com/capstone/recharge/
├── design-document.md
├── README.md
├── .gitignore
└── pom.xml
```

The project structure will evolve as additional components are implemented.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21
- Maven, or the Maven Wrapper included with the project
- Git

## Design Documentation

The `design-document.md` file contains the business rules, edge cases, proposed system components and design decisions for the project.

## Project Status

**Status:** Initial Development

The initial development phase focuses on establishing the project structure and implementing the first core flows:

- Subscriber registration.
- Core-balance recharge and validity renewal.
- Unlimited-pack purchase and validity calculation.

Additional business rules will be implemented and tested incrementally.

## Future Scope

- Persistent storage for subscriber accounts, packs and transactions.
- REST APIs for subscriber registration, recharges and usage processing.
- Automated testing of complex stacking and billing scenarios.
- Improved transaction consistency and concurrency handling.
- Comprehensive ledger generation and reporting.
