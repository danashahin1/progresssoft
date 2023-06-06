----------------------------------------------------Problem:----------------------------------------------------

ClusteredData Warehouse
Suppose you are part of a scrum team developing data warehouse for Bloomberg to analyze FX deals. One of customer stories is to accept deals details from and persist them into DB.


Request logic as following:

Request Fields(Deal Unique Id, From Currency ISO Code "Ordering Currency", To Currency ISO Code, Deal timestamp, Deal Amount in ordering currency).
Validate row structure.(e.g: Missing fields, Type format..etc. We do not expect you to cover all possible cases but we'll look to how you'll implement validations)
System should not import same request twice.
No rollback allowed, what every rows imported should be saved in DB.

Deliverables should be ready to work including:

Use Actual Db, you can select between (Postgres, MySql or MongoDB)
Workable deployment including sample file (Docker Compose).
Maven or Gradle project is required for full source code.
Proper error/exception handling.
Proper Logging.
Proper unit testing with respected Coverage.
Proper documentation using md.
Delivered over Githhub.com.
Makefile to streamline running application (plus).

------------------------------------------------Procedure:-------------------------------------------------

- MySQL Database was created . 
- Two Tables were created (Deals and invalid_Deals).
- since the requirement indicates that there is no rollback (invalid_deals) was created to store invalid data
- data validation was done on (date format , currency code , id duplication , amount value...ect).
- validation done on two levels : manual check (before inserting to database) , entity validation when persisting the deal.
- valid rows will be inserted into deals table while invalid ones will ve inserted into invalid_deals table.
- Exceptions handling was overriden to catch some exceptions like (constraint violation exception ) or another new exceptions.
- logs were used before each step .
-test cases were written (for inserting valid/invalid rows)

---------------------------------------------Database scripts-------------------------------------------------

                                        --------Deals table-----------

   CREATE TABLE `deals` (
  `ID` varchar(100) NOT NULL,
  `FROM_CURRENCY` varchar(45) DEFAULT NULL,
  `TO_CURRENCY` varchar(45) DEFAULT NULL,
  `DEAL_TIME` datetime DEFAULT NULL,
  `DEAL_AMOUNT` bigint DEFAULT '0',
  PRIMARY KEY (`ID`)
)

                                    -----------invalid_deals table------------

CREATE TABLE `invalid_deals` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `DEAL_ID` varchar(100) DEFAULT NULL,
  `FROM_CURRENCY` varchar(45) DEFAULT NULL,
  `TO_CURRENCY` varchar(45) DEFAULT NULL,
  `DEAL_TIME` datetime DEFAULT NULL,
  `DEAL_AMOUNT` bigint DEFAULT '0',
  PRIMARY KEY (`ID`)
)

 ---------------------------------------------------------------------------------------------------------------------                       


