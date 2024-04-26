import React, { useEffect, useState } from "react";
import { isAuthenticated } from "../utils";
import UnauthenticatedPage from "./UnauthenticatedPage";

interface Props {
  Component: React.FC;
}

const ProtectedRoute: React.FC<Props> = ({ Component }) => {
  const [isLoading, setIsLoading] = useState(true);
  const [isAuthenticatedUser, setIsAuthenticatedUser] = useState(false);

  useEffect(() => {
    const checkAuthentication = async () => {
      const isAuthenticatedResult = await isAuthenticated(
        localStorage.getItem("token")
      );
      setIsAuthenticatedUser(isAuthenticatedResult);
      setIsLoading(false);
    };

    checkAuthentication();
  }, []);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return isAuthenticatedUser ? <Component /> : <UnauthenticatedPage />;
};

export default ProtectedRoute;
