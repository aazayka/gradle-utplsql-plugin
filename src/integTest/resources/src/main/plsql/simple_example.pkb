CREATE OR REPLACE PACKAGE BODY TESTING.SIMPLE_EXAMPLE
AS
  PROCEDURE DOUBLE_IT(V IN NUMBER, W OUT NUMBER)
  IS
    BEGIN
      W := 2 * V;
    END DOUBLE_IT;

  FUNCTION TRIPLE_IT(Y IN NUMBER)
    RETURN NUMBER
  IS
    BEGIN
      RETURN 3 * Y;
    END TRIPLE_IT;

END SIMPLE_EXAMPLE;
/
