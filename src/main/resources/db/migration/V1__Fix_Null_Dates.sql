-- Update null dates in income table to current date
UPDATE tbl_incomes SET date = CURRENT_DATE WHERE date IS NULL;

-- Update null dates in expense table to current date
UPDATE tbl_expenses SET date = CURRENT_DATE WHERE date IS NULL;

