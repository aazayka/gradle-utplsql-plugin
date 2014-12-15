CREATE OR REPLACE PACKAGE UT_FAILING
IS
   PROCEDURE ut_setup;
   PROCEDURE ut_teardown;
 
   -- For each program to test...
   PROCEDURE UT_FAILING;
END UT_FAILING;
/
