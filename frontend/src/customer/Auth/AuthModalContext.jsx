import { createContext } from "react";

export const AuthModalContext = createContext({
  openAuthModal: () => {},
  setOpenAuthModal: () => {},
  openAuthModalState: false,
});
