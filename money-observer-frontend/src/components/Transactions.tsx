import React, { useState } from "react";
import { Button } from "react-bootstrap";
import ExpenseCreationForm from "./TransactionCreationForm";
import {
  deleteExpense,
  deleteIncome, formatDate,
  getCategories,
  getExpenses,
  getIncome,
  getSavings,
  getTotalncome,
} from "../utils";

const token = localStorage.getItem("token");

interface Transaction {
  id: number;
  description: string;
  amount: number;
  time: string;
}

interface TransactionProps {
  categoryId?: number;
  transactions: Transaction[];
  setTransactions: React.Dispatch<React.SetStateAction<Transaction[]>>;
  setCategories: React.Dispatch<React.SetStateAction<any[]>>;
  setTotalIncome: (value: React.SetStateAction<number>) => void;
  setSavings: React.Dispatch<React.SetStateAction<number>>;
}

const Transactions: React.FC<TransactionProps> = ({
  transactions,
  setTransactions,
  categoryId,
  setCategories,
  setTotalIncome,
  setSavings,
}) => {
  const [showTransactionForm, setShowTransactionForm] = useState(false);

  const handleDeleteTransaction = async (transactionId: number) => {
    let response;
    if (categoryId !== undefined) {
      await deleteExpense(token, transactionId);
      response = await getExpenses(token, categoryId);
    } else {
      await deleteIncome(token, transactionId);
      response = await getIncome(token);
    }
    if (response.ok) {
      setTotalIncome(await getTotalncome(token));
      setTransactions(await response.json());
      setSavings(await getSavings(token));
      const categoriesResponse = await getCategories(token);
      setCategories(await categoriesResponse.json());
    }
  };

  const color = categoryId !== undefined ? "red" : "green";

  return (
    <div>
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">Description</th>
            <th scope="col">Amount</th>
            <th scope="col">Date</th>
            <th scope="col">Actions</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((transaction, index) => (
            <tr key={index}>
              <td>{transaction.description}</td>
              <td style={{ color: color }}>${transaction.amount}</td>
              <td>{formatDate(transaction.time)}</td>
              <td>
                <Button
                  variant="danger"
                  onClick={() => {
                    handleDeleteTransaction(transaction.id);
                  }}
                >
                  Delete
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="d-flex justify-content-end">
        <Button variant="primary" onClick={() => setShowTransactionForm(true)}>
          Add {categoryId == undefined ? "income" : "expense"}
        </Button>
      </div>
      <ExpenseCreationForm
        handleClose={async () => {
          setShowTransactionForm(false);
          setSavings(await getSavings(token));
        }}
        show={showTransactionForm}
        setTransactions={setTransactions}
        categoryId={categoryId}
        setCategories={setCategories}
        setTotalIncome={setTotalIncome}
      />
    </div>
  );
};

export default Transactions;
