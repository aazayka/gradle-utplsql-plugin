CREATE OR REPLACE PACKAGE UT_BROKEN
IS
   PROCEDURE ut_setup;
   PROCEDURE ut_teardown;
 a
   -- For each program to test...
   PROCEDURE UT_BROKEN;
END UT_BROKEN;
/