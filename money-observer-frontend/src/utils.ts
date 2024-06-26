const apiUrl = "http://localhost:8080/api";

export const currencyFormatter = new Intl.NumberFormat(undefined, {
  currency: "usd",
  style: "currency",
  maximumFractionDigits: 0,
});

export const getCategories = async (token: string | null) => {
  const response = await fetch("http://localhost:8080/api/categories", {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
  return response;
};

export const isAuthenticated = async (token: string | null) => {
  const response = await fetch("http://localhost:8080/api/users", {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  return response.ok;
};

export const getSavings = async (token: string | null) => {
  const response = await fetch(`${apiUrl}/expenses/totalSpending`, {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  const data = await response.json();
  const totalIncome = await getTotalncome(token);

  return totalIncome - (isNaN(data.totalSpending)?0:data.totalSpending);
};

export const formatDate= (dateString: string): string =>{
  const date = new Date(dateString);
  const year = date.getFullYear().toString();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');

  return `${year}-${month}-${day} ${hours}:${minutes}`;
}

export const getUsername = async (token: string | null) => {
  const response = await fetch("http://localhost:8080/api/users", {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
  const data = await response.json();

  return data.username;
};

export const getIncome = async (token: string | null) => {
  const response = await fetch("http://localhost:8080/api/income", {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  return response;
};

export const deleteExpense = async (
  token: string | null,
  expenseId: number
) => {
  await fetch(`http://localhost:8080/api/expenses/${expenseId}`, {
    method: "DELETE",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
};
export const deleteIncome = async (token: string | null, incomeId: number) => {
  await fetch(`http://localhost:8080/api/income/${incomeId}`, {
    method: "DELETE",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
};
export const getExpenses = async (token: string | null, categoryId: number) => {
  const response = await fetch(
    `http://localhost:8080/api/categories/${categoryId}/expenses`,
    {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return response;
};

export const getTotalncome = async (token: string | null) => {
  const response = await fetch(`http://localhost:8080/api/income/total`, {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
  const data = await response.json();
  return isNaN(data.totalIncome)?0:data.totalIncome;
};
