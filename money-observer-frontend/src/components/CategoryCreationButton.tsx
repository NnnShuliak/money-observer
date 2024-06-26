import { Card, CardBody, CardTitle } from "react-bootstrap";

interface Props {
  onClick: () => void;
}

const CategoryCreationButton: React.FC<Props> = ({ onClick }) => {
  return (
    <div onClick={onClick} className="category-button">
      <Card className={"secondary"}>
        <CardBody>
          <CardTitle className="d-flex justify-content-center align-items-baseline fw-normal mb-1">
            <div className="me">Create category</div>
          </CardTitle>
        </CardBody>
      </Card>
    </div>
  );
};

export default CategoryCreationButton;
