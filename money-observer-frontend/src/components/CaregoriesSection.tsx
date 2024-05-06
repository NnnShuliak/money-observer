// components/CategorySection.js
import React, {useState, useEffect} from "react";
import Category from "./Category";
import CategoryCreationForm from "./CategoryCreationForm";
import CategoryCreationButton from "./CategoryCreationButton";
import {
    currencyFormatter,
    getCategories,
    getExpenses,
    getSavings,
    getTotalncome,
} from "../utils";

interface Props {
    setTransactions: (value: React.SetStateAction<any[]>) => void;
    setSelectedGroup: (value: React.SetStateAction<number>) => void;
    setCategories: React.Dispatch<React.SetStateAction<any[]>>;
    setTotalIncome: React.Dispatch<React.SetStateAction<number>>;
    setSavings: React.Dispatch<React.SetStateAction<number>>;
    savings: number;
    totalIncome: number;
    selectedGroup: number;
    categories: any[];
}

let token = localStorage.getItem("token");

const CategorySection = ({
                             setTransactions,
                             setSelectedGroup,
                             setCategories,
                             setTotalIncome,
                             setSavings,
                             savings,
                             totalIncome,
                             categories,
                             selectedGroup,
                         }: Props) => {
    const [showCategoryForm, setShowCategoryForm] = useState(false);

    useEffect(() => {
        fetchCategories();
    }, []);

    const fetchCategories = async () => {
        try {
            token = localStorage.getItem("token");
            console.log("jwt: " + token);
            const categoriesResponse = await getCategories(token);
            const categoriesData = await categoriesResponse.json();
            setCategories(categoriesData);
            setSavings(await getSavings(token));
            setTotalIncome(await getTotalncome(token));
        } catch (error) {
            console.error("Error fetching categories:", error);
        }
    };

    const handleGroupClick = async (categoryId: number) => {
        try {
            const response = await getExpenses(token, categoryId);
            const data = await response.json();
            setTransactions(data);
            setSelectedGroup(categoryId);
        } catch (error) {
            console.error("Error fetching transactions:", error);
        }
    };

    const handleIncomeClick = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/income`, {
                method: "GET",
                headers: {
                    "Content-type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });
            if (response.ok) {
                const data = await response.json();
                setTransactions(data);
                setSelectedGroup(0);
            }
        } catch (error) {
            console.error("Error fetching transactions:", error);
        }
    };

    const handleDeleteCategory = async (categoryId: number) => {
        try {
            const response = await fetch(
                `http://localhost:8080/api/categories/${categoryId}`,
                {
                    method: "DELETE",
                    headers: {
                        "Content-type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            if (response.ok) {
                setSavings(await getSavings(token));
                setCategories(
                    categories.filter((category: any) => category.id !== categoryId)
                );
            } else {
                throw new Error("Failed to delete category");
            }
        } catch (error) {
            console.error("Error deleting category:", error);
            alert("Failed to delete category");
        }
    };

    return (
        <div>
            <Category
                title="Income "
                amount={totalIncome}
                selected={selectedGroup == 0}
                onClick={() => handleIncomeClick()}
            />
            <div className="ms-3 savings">Savings = {currencyFormatter.format(savings)}</div>

            <div>
                {categories.map((category: any) => (
                <Category
                    key={category.id}
                    title={category.title}
                    amount={category.amount}
                    max={category.max}
                    selected={selectedGroup == category.id}
                    onClick={() => handleGroupClick(category.id)}
                    onDelete={() => handleDeleteCategory(category.id)}
                />
            ))}
                <CategoryCreationButton onClick={() => setShowCategoryForm(true)}/>
                <CategoryCreationForm
                    show={showCategoryForm}
                    handleClose={() => {
                        setShowCategoryForm(false);
                    }}
                    setCategories={setCategories}
                    categories={categories}
                />
            </div>
        </div>
    );
};

export default CategorySection;
