import React from "react";

interface UserInfoProps {
  username: string;
  email: string;
}

const UserInfo: React.FC<UserInfoProps> = ({ username, email }) => {
  return (
    <div>
      <h4>User Information</h4>
      <p>Username: {username}</p>
      <p>Email: {email}</p>
    </div>
  );
};

export default UserInfo;
