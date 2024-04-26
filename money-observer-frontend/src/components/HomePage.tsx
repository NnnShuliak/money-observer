import { useState } from "react";
import "../styles/HomePage.css";
import Transactions from "./Transactions";
import CategorySection from "./CaregoriesSection";
import Navigation from "./Navigation";

const HomePage: React.FC = () => {
  const [selectedGroup, setSelectedGroup] = useState(NaN);
  const [transactions, setTransactions] = useState<any[]>([]);
  const [categories, setCategories] = useState<any[]>([]);
  const [totalIncome, setTotalIncome] = useState(NaN);
  const [savings, setSavings] = useState(NaN);

  return (
    <>
      <Navigation />
      <div className="container">
        <div className="row">
          <div className="col-md-4 categories-section">
            <CategorySection
              setTotalIncome={setTotalIncome}
              totalIncome={totalIncome}
              setCategories={setCategories}
              categories={categories}
              setSavings={setSavings}
              savings={savings}
              setTransactions={setTransactions}
              setSelectedGroup={setSelectedGroup}
              selectedGroup={selectedGroup}
            />
          </div>
          <div className="col-md-8">
            <div className="row">
              {Number.isNaN(selectedGroup) || (
                <Transactions
                  transactions={transactions}
                  setTransactions={setTransactions}
                  categoryId={selectedGroup == 0 ? undefined : selectedGroup}
                  setCategories={setCategories}
                  setTotalIncome={setTotalIncome}
                  setSavings={setSavings}
                />
              )}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default HomePage;
