
-- 🔹 2. Insert INCOME data
INSERT INTO tbl_transactions (
id,
amount,
created_at,
date,
icon,
name,
type,
updated_at,
category_id,
profile_id
)
SELECT
id,
amount,
created_at,
date,
icon,
name,
'INCOME',
updated_at,
category_id,
profile_id
FROM tbl_incomes;

-- 🔹 3. Insert EXPENSE data (avoid ID conflict)
INSERT INTO tbl_transactions (
id,
amount,
created_at,
date,
icon,
name,
type,
updated_at,
category_id,
profile_id
)
SELECT
id + (SELECT COALESCE(MAX(id), 0) FROM tbl_incomes),
amount,
created_at,
date,
icon,
name,
'EXPENSE',
updated_at,
category_id,
profile_id
FROM tbl_expenses;

-- 🔹 4. (Optional) Create views for backward compatibility

-- Income View
CREATE OR REPLACE VIEW tbl_incomes_view AS
SELECT * FROM tbl_transactions WHERE type = 'INCOME';

-- Expense View
CREATE OR REPLACE VIEW tbl_expenses_view AS
SELECT * FROM tbl_transactions WHERE type = 'EXPENSE';

-- =========================================
-- END OF MIGRATION
-- =========================================
