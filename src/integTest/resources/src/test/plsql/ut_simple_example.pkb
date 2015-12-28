CREATE OR REPLACE PACKAGE BODY UT_SIMPLE_EXAMPLE
AS
  PROCEDURE UT_SETUP
  IS
    BEGIN
      NULL;
    END UT_SETUP;

  PROCEDURE UT_TEARDOWN
  IS
    BEGIN
      NULL;
    END UT_TEARDOWN;

  PROCEDURE UT_DOUBLE_IT
  IS
    A NUMBER;
    BEGIN

      SIMPLE_EXAMPLE.DOUBLE_IT(NULL, A);
      utAssert.isnull('Null value', A);

      SIMPLE_EXAMPLE.DOUBLE_IT(1, A);
      utAssert.eq('Typical value', A, 2);

    END UT_DOUBLE_IT;

  PROCEDURE UT_TRIPLE_IT
  IS
    BEGIN

      utAssert.isnull('Null value', SIMPLE_EXAMPLE.TRIPLE_IT(NULL));
      utAssert.eq('Typical value', SIMPLE_EXAMPLE.TRIPLE_IT(1), 3);

    END UT_TRIPLE_IT;

END UT_SIMPLE_EXAMPLE;
/
