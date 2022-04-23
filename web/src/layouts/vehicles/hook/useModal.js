import { useState } from "react";
import { useSelector } from "react-redux";

export default function useModal(initOpen){
  const [open, setOpen] = useState(initOpen);
  return [open, setOpen]
}