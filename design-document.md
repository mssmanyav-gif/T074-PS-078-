# PS-078: Prepaid Recharge, Pack Stacking and Validity Engine

## 1. Project Overview

The Prepaid Recharge, Pack Stacking and Validity Engine is a backend application that simulates the recharge and usage-management operations of a prepaid telecommunications system.

The engine manages subscribers, recharge transactions, pack validity, data quotas, voice and SMS usage, roaming, core balances and subscriber lifecycle transitions. It applies predefined business rules to determine which pack should serve an event, how much quota or balance should be deducted, and how the transaction should be recorded.

The system will also maintain a ledger that allows transactions and subscriber balances to be examined over a specified date range.

## 2. Objectives

- Register subscribers and maintain their account information.
- Process core-balance recharges and prepaid pack purchases.
- Calculate charges and deduct the appropriate quota or balance.
- Manage data rollover and fair usage policy (FUP).
- Handle roaming and loyalty bonuses.
- Manage subscriber lifecycle transitions.
- Generate transaction and usage ledgers.
- Handle exception cases and insufficient-balance scenarios consistently.

## 3. Technology Stack

| Component | Technology |
|---|---|
| Programming language | Java 21 |
| Backend framework | Spring Boot |
| Build tool | Maven |
| Version control | Git |
| Initial testing | JUnit 5 |
| Initial storage | In-memory Java collections |
| Future persistent storage | MongoDB, subject to project requirements |

The first implementation will focus on the core business logic and sample-input testing. Database persistence and additional REST API endpoints can be introduced incrementally.

## 4. Business Rules

### BR-01: Pack Validity and Stacking

A 28-day pack applied on day D expires at 23:59:59 on day D+27.

When a pack of the same type is purchased while that pack type is active, the new pack must be queued. Its validity begins immediately after the currently active pack expires.

Pack activation and expiry timestamps must be calculated consistently.

### BR-02: Data Stacking Precedence

Data add-ons must be consumed in the following order:

1. Earliest expiry timestamp first.
2. If expiry timestamps are identical, the add-on with the smallest remaining quota is consumed first.
3. If expiry and remaining quota are identical, the pack with the lowest pack ID is consumed first.
4. The unlimited pack's daily data quota is consumed only after eligible data add-ons have been exhausted.

While roaming, an active roaming add-on's data must be consumed before other data allowances.

### BR-03: Unlimited-Pack Daily Quota

The following unlimited packs provide the specified daily data quotas:

| Pack Price | Validity | Daily Data Quota |
|---|---|---|
| ₹299 | 28 days | 1.5 GB |
| ₹359 | 28 days | 2 GB |
| ₹719 | 56 days | 2.5 GB |

Daily quotas reset at 00:00 IST.

Unused daily quota is lost unless it qualifies for rollover under BR-04.

### BR-04: Data Rollover

Eligible unused daily quota is added to the rollover bucket.

The rollover bucket must never exceed 200 GB. Any unused quota exceeding the available rollover capacity is discarded.

Rollover data is consumed only after the current day's unlimited-pack quota has been exhausted and only while an unlimited pack is active.

The rollover bucket is forfeited one day after the last unlimited pack expires.

The implementation must define the precise rollover cutoff timestamp and the order in which rollover is calculated and expired.

### BR-05: Post-Quota Data Handling

When both the daily quota and eligible rollover data are exhausted while an unlimited pack is active, data usage is served at 64 kbps without additional charges.

Such usage events must be tagged `FUP`.

When no unlimited pack is active, pay-per-use data is charged at ₹0.50 per MB. The chargeable quantity is rounded up to the next whole MB.

For example, 0.3 MB of usage is billed as 1 MB, costing ₹0.50.

### BR-06: Voice and SMS Charging

An active unlimited pack includes unlimited voice and 100 SMS per day.

The 101st SMS and each subsequent SMS that day are charged at ₹1 per message from the core balance.

Without an unlimited pack:

- Voice calls cost ₹1 per started minute.
- SMS messages cost ₹1 each.

A voice call that lasts any fraction of a minute is charged for the corresponding whole minute.

### BR-07: National Roaming

National roaming voice is free while an unlimited pack is active.

Without an unlimited pack, roaming voice outside the subscriber's home circle costs ₹1.50 per started minute.

An active roaming add-on costs ₹49 and has seven-day validity. It provides:

- Free roaming voice.
- An additional 1 GB data allowance while roaming.

Roaming add-on data must be consumed before other eligible data allowances while roaming.

The implementation must explicitly define how events are handled when roaming-add-on validity expires during an ongoing session.

### BR-08: Core Balance Validity

The core balance has a validity period of 30 days.

A recharge of ₹10 or more renews core-balance validity.

A subscriber becomes eligible to enter `GRACE` when both core-balance validity and all pack validities have expired.

The exact expiry timestamp for initial balances and subsequent recharges must follow the validity convention documented by the project.

### BR-09: Subscriber Lifecycle

The subscriber lifecycle has four states:

- `ACTIVE`: Normal service is available subject to pack validity and balance.
- `GRACE`: Lasts 15 days. Only incoming service is allowed; outgoing usage events are not accepted.
- `SUSPENDED`: Lasts 60 days. Events are not accepted, and the balance remains frozen.
- `DEACTIVATED`: Entered after 75 days in GRACE and SUSPENDED combined. The core balance is forfeited.

A recharge during `GRACE` or `SUSPENDED` restores the subscriber to `ACTIVE` and resumes core-balance validity.

The transition logic must prevent expired subscribers from remaining indefinitely in earlier states.

### BR-10: Insufficient Balance

An event requiring a core-balance deduction must be rejected if its complete charge would make the balance negative.

The rejection reason must be `NO_BALANCE`.

Partial service is not allowed.

The engine must validate the full charge before committing any deductions or balance changes. Rejected events must not leave partially modified subscriber or pack state.

### BR-11: Loyalty Bonus

Every third consecutive 28-day unlimited pack purchased without a gap earns a 2 GB data add-on with seven-day validity.

The consecutive-pack counter resets when a gap of at least one day occurs.

The implementation must define consecutive-pack continuity using pack validity intervals rather than simply counting purchases.

Queued packs must be evaluated according to their intended activation and continuity, not merely their purchase timestamps.

### BR-12: Events Crossing Midnight

An event that crosses midnight must be billed entirely on the day it started.

The engine must evaluate the packs and applicable billing rules at the event's start time.

For example, a voice call starting at 23:58 and ending at 00:03 is evaluated against the pack state and billing rules applicable at 23:58.

The event must not be split into separate billing days.

## 5. Edge Cases and Acceptance Criteria

### EC-01: Pack Applied at 23:59:59

**Input:** A 28-day pack is applied at 23:59:59 on day D.

**Expected:** The pack expires at 23:59:59 on day D+27, not day D+28.

### EC-02: Identical Data Add-ons

**Input:** Two data add-ons have identical expiry timestamps and identical remaining quotas.

**Expected:** The add-on with the lower pack ID is consumed first.

### EC-03: Rollover Bucket at Maximum Capacity

**Input:** The rollover bucket already contains 200 GB, and additional unused daily quota becomes eligible for rollover.

**Expected:** The bucket remains at 200 GB. Excess quota is discarded.

### EC-04: Fractional Pay-per-Use Data

**Input:** A subscriber without an unlimited pack consumes 0.3 MB of data.

**Expected:** The usage is rounded up to 1 MB and charged ₹0.50.

### EC-05: Recharge on GRACE Day 15

**Input:** A subscriber receives a ₹10 recharge at 23:59 on the 15th day of GRACE.

**Expected:** If the recharge is processed before deactivation, the subscriber returns to `ACTIVE`, the core balance increases by ₹10, and core-balance validity is renewed.

The event ordering at the GRACE-to-SUSPENDED boundary must be deterministic.

### EC-06: Roaming Add-on Expires During a Data Session

**Input:** A roaming data session begins while the roaming add-on is active but continues beyond its expiry.

**Expected:** The engine must follow a documented session-timing policy. The policy must specify whether the entire event uses the state at its start or whether usage is divided at the expiry boundary.

### EC-07: Insufficient Balance for the 101st SMS

**Input:** The subscriber has ₹0.99 in core balance, has already sent 100 SMS under an unlimited pack, and attempts to send the 101st SMS.

**Expected:** The SMS is rejected with `NO_BALANCE`. No balance is deducted, and no partial service is provided.

### EC-08: Third Consecutive Pack Queued in Advance

**Input:** A third 28-day unlimited pack is purchased and queued before the second pack begins.

**Expected:** The engine evaluates the continuity of the three packs using their validity intervals. If all continuity conditions are satisfied, the subscriber receives the 2 GB loyalty add-on with seven-day validity.

### EC-09: Voice Call Crossing Midnight

**Input:** A voice call starts at 23:58 and ends at 00:03.

**Expected:** The entire event is billed using the applicable rules and pack state at the event's start time. The event is not split across two billing days.

## 6. Proposed System Components

### Subscriber Management

Responsible for registering subscribers and maintaining their home circle, activation date, core balance, balance validity and lifecycle status.

### Recharge Management

Responsible for processing core-balance recharges, unlimited packs and add-on purchases.

### Pack Management

Responsible for pack creation, activation, queuing, expiry and quota tracking.

### Usage Processing

Responsible for processing voice, data and SMS events, determining applicable charges, selecting eligible packs and recording deductions.

### Lifecycle Management

Responsible for evaluating validity expiry and transitioning subscribers between `ACTIVE`, `GRACE`, `SUSPENDED` and `DEACTIVATED`.

### Ledger Management

Responsible for recording recharge transactions, pack purchases, usage events, charges, deductions, pack identifiers and relevant balance snapshots.

## 7. Initial Implementation Scope

The initial development phase will include:

1. Subscriber registration.
2. Core-balance recharge and validity renewal.
3. Basic unlimited-pack purchase and validity calculation.

These flows will be tested using sample inputs before the implementation expands to data stacking, usage charging, lifecycle management and ledger generation.

## 8. Design Decisions Requiring Explicit Definitions

The following items require clarification or a documented implementation decision:

1. Whether pack validity uses calendar-day boundaries in IST or a duration measured from the exact application timestamp.
2. The precise expiry timestamp for core-balance validity and the initial balance.
3. The exact rollover accumulation and forfeiture timestamps.
4. Whether roaming events use the add-on state at the start of the event or split usage when validity expires.
5. Whether a subscriber with an active pack but expired core-balance validity remains `ACTIVE`.
6. Whether queued packs qualify for loyalty based on purchase order, activation order or contiguous validity.
7. How recharges are ordered against lifecycle transitions at the exact boundary between GRACE and SUSPENDED.
8. How concurrent recharge and usage requests are serialized to prevent inconsistent balance deductions.

These decisions must be documented before the corresponding business rules are implemented.

## 9. Expected Outcome

The completed engine will provide a deterministic way to manage prepaid subscriber accounts, apply recharge and usage rules, maintain accurate balances and quotas, and produce an auditable ledger.

The implementation will be developed incrementally, with automated tests for business rules and edge cases.
