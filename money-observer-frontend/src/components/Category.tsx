import React from "react";
import {
  Button,
  Card,
  CardBody,
  CardTitle,
  ProgressBar,
} from "react-bootstrap";
import { currencyFormatter } from "../utils";
import "../styles/Category.css";
import "../index.css";

interface Props {
  title: string;
  amount: number;
  max?: number;
  selected: boolean;
  onClick: () => void;
  onDelete?: () => void;
}

const Category: React.FC<Props> = ({
  title,
  amount,
  max,
  selected,
  onClick,
  onDelete,
}) => {
  const formattedAmount = currencyFormatter.format(amount);
  const formattedMax = max ? currencyFormatter.format(max) : "";

  let cardClassName = selected ? "selected-color" : "bg-light";

  return (
    <div onClick={onClick}>
      <Card className={cardClassName+" m-3"}>
        <CardBody>
          <CardTitle className="d-flex justify-content-between align-items-baseline fw-normal mb-1">
            <div className="me-2">{title}</div>
            <div className="d-flex align-items-baseline">
              {formattedAmount}
              {max && (
                <span className="text-muted fs-6 ms-1">/ {formattedMax}</span>
              )}
              {onDelete && (
                <Button variant="danger" onClick={onDelete} className="ms-2">
                  Delete
                </Button>
              )}
            </div>
          </CardTitle>
          {max && (
            <ProgressBar
              className="rounded-pill"
              variant={getVariant(amount, max)}
              min={0}
              max={max}
              now={amount}
            />
          )}
        </CardBody>
      </Card>
    </div>
  );
};

const getVariant = (amount: number, max: number) => {
  const ratio = amount / max;
  if (ratio < 0.5) return "primary";
  if (ratio < 0.75) return "warning";
  return "danger";
};

export default Category;
