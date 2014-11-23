create or replace package body UT_SIMPLE_EXAMPLE
as
   procedure UT_SETUP
   IS
   BEGIN
     NULL;
   END UT_SETUP;
   
   procedure UT_TEARDOWN   
   IS
   BEGIN
     NULL;
   END UT_TEARDOWN;
   
   procedure UT_DOUBLE_IT
   IS
     A NUMBER;
   BEGIN
   
     SIMPLE_EXAMPLE.DOUBLE_IT(NULL, A);
     utAssert.isnull('Null value', A);

     SIMPLE_EXAMPLE.DOUBLE_IT(1, A);
     utAssert.eq('Typical value', A, 2);
   
   END UT_DOUBLE_IT;
   
   procedure UT_TRIPLE_IT
   IS
   BEGIN
   
     utAssert.isnull('Null value', SIMPLE_EXAMPLE.TRIPLE_IT(null));
     utAssert.eq('Typical value', SIMPLE_EXAMPLE.TRIPLE_IT(1), 3);     
   
   END UT_TRIPLE_IT;
   
end UT_SIMPLE_EXAMPLE;
/