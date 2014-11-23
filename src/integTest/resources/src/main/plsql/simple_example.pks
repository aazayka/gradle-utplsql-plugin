create or replace package SIMPLE_EXAMPLE
as
   procedure DOUBLE_IT(V IN number,W OUT number);
   function TRIPLE_IT(Y IN number) return number;
   
end SIMPLE_EXAMPLE;
/