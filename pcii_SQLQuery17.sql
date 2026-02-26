CREATE SEQUENCE electronics_seq
    START WITH 1
    INCREMENT BY 1;
GO

CREATE SEQUENCE clothing_seq
    START WITH 1
    INCREMENT BY 1;
GO

CREATE TABLE electronics (
    sku VARCHAR(20) PRIMARY KEY,
    category VARCHAR(50),
    prodName VARCHAR(100),
    quantity INT,
    unitCost DECIMAL(10,2),
    margin INT,
    weight DECIMAL(5,2)
);

CREATE TABLE clothing (
    sku VARCHAR(20) PRIMARY KEY,
    category VARCHAR(50),
    prodName VARCHAR(100),
    quantity INT,
    unitCost DECIMAL(10,2),
    margin INT,
    volume Decimal(10,2)
);
GO

CREATE TRIGGER trg_electronics_insert
ON electronics
INSTEAD OF INSERT
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO electronics (sku, category, prodName, quantity, unitCost, margin, weight)
    SELECT
        'SKU-' + RIGHT('0000' + CAST(NEXT VALUE FOR electronics_seq AS VARCHAR(4)), 4),
        category, prodName, quantity, unitCost, margin, weight
    FROM inserted;
END;
GO

CREATE TRIGGER trg_clothing_insert
ON clothing
INSTEAD OF INSERT
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO clothing (sku, category, prodName, quantity, unitCost, margin, volume)
    SELECT
        'SKU-' + RIGHT('0000' + CAST(NEXT VALUE FOR clothing_seq AS VARCHAR(4)), 4),
        category, prodName, quantity, unitCost, margin, volume
    FROM inserted;
END;
GO

INSERT INTO electronics (category, prodName, quantity, unitCost, margin, weight) VALUES
('Laptop','MacBook Pro',10,1300.00,50,2.0);

INSERT INTO clothing (category, prodName, quantity, unitCost, margin, volume) VALUES
('T-Shirt','Basic White Tee',100,15.00,40,1500);

select * from electronics;
select * from clothing;

--use master;
--drop database pciiDB17;
--drop table electronics;
--drop table clothing;
--drop sequence electronics_seq;
--drop sequence clothing_seq;
--drop trigger trg_electronics_insert;
--drop trigger trg_clothing_insert;
