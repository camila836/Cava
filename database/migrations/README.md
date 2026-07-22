# Migraciones aplazadas

Estas migraciones están ordenadas para trazabilidad, pero corresponden a la
Fase 12 y no forman parte del instalador `00`–`03` de la primera versión.

No deben ejecutarse hasta que existan decisiones funcionales, Models, DAO,
pruebas y autorización específica para cada módulo. Su numeración no afirma
que hayan sido aplicadas en ningún entorno.

`F008__autenticacion_base.sql` y `F009__unidades_medida_unicas.sql` son
migraciones de fases ya autorizadas y no pertenecen al bloque futuro V001-V004.
F009 exige respaldo previo y ejecución administrativa controlada.
