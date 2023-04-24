# Transaction API

This is a RESTful API that allows you to manage transactions and retrieve related information.

## Installation

To install the API, you can follow these steps:

1. Clone the repository: `git clone https://github.com/RodrigoRipoll/transaction-api.git`
2. Run the application with: 
   - `java17` and `springboot3` are use in this project
   -  with docker `use docker-compose up` or `docker-compose up --build`

## Usage

Once you have the API up and running, you can use it to manage transactions and retrieve related information.

## OpenAPI Specification

In the path `/src/main/resources` a `swagger.yaml` can be found with the contracts stipulated for the API.
This will show you how:
- create a Transaction
- request Information by Transaction-Type 
- request Sum of Amounts from Related Transactions by Transaction-Id.

## Contributing
If you would like to contribute to the project, feel free to submit a pull request with your changes.

## License
This project is licensed under the MIT License. See the LICENSE file for more information.
