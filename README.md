## Instruction how to use:

This application uses H2 Database, Java 21, and Maven. Make sure you have those installed before launching the app.

1. Launch H2 database*
2. In the main directory of the project using command-line use following commands:

```
mvn clean
mvn install
mvn spring-boot:run
```

\* the database schema is automatically generated during the startup of the app

If you want to use database queries, you can use the following link (assuming that the app is running in the background)
http://localhost:8080/h2-console

Login credentials for database:

```
username: sa
password: password
```

## REST API endpoints:

1. Create a new product

(POST)

```
localhost:8080/api/products
```

example body:

```json
{
  "name": "Cookies",
  "description": "Contain chocolate",
  "price": 2.99,
  "currencyCode": "EUR"
}
```

---

2. Get all products

(GET)

```
localhost:8080/api/products
```

---

3. Update product data

(PUT)

```
localhost:8080/api/products/{productID}
```

example body:

```json
{
  "name": "Biscuits",
  "description": "Contain raisins",
  "price": 8,
  "currencyCode": "USD"
}
```

---

4. Create a new promo code.

(POST)

```
localhost:8080/api/promocodes
```

example body:

```json
{
  "code": "CODE_PERCENTAGE",
  "promoCodeType": "percentage",
  "discount": "25",
  "currencyCode": "EUR",
  "maxUsages": 32,
  "expirationDate": "2024-08-31"
}
```
or
```json
{
"code": "CODE_VALUE",
"promoCodeType": "value",
"discount": "0.25",
"currencyCode": "EUR",
"maxUsages": 32,
"expirationDate": "2024-08-31"
}
```
\* promoCodeType can be either "percentage" or "value"
\** discount is either a percentage value or a discount value, depending on the type of promocode


---

5. Get all promo codes.

(GET)

```
localhost:8080/api/promocodes
```

---

6. Get one promo code's details by providing the promo code. The detail should also contain the number of usages.

(GET)

```
localhost:8080/api/promocodes/{code}
```

---

7. Get the discount price by providing a product and a promo code.

(GET)

```
localhost:8080/api/products/{productID}/calculateDiscount?code={code}
```

---

8. Simulate purchase

(POST)

```
localhost:8080/api/purchases?productID={productID}&code={code}
```

\* the code parameter is optional and can be ignored

---

9. [Optional] A sales report: number of purchases and total value by currency (see below)

(GET)

```
localhost:8080/api/purchases/sales-report
```

---

Also you can use included postman collection (`task_sii.postman_collection.json`).
