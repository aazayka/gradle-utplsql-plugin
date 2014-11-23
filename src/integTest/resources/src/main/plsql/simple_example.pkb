create or replace package body SIMPLE_EXAMPLE
as
   procedure DOUBLE_IT(V IN number,W OUT number)
   IS
   BEGIN
     W := 2*V;
   END DOUBLE_IT;
   
   function TRIPLE_IT(Y IN number) return number
   IS
   BEGIN
     RETURN 3*Y;
   END TRIPLE_IT;
   
end SIMPLE_EXAMPLE;
/