import { createContext } from "react";
import User from "../../classes/User";

export const UserContext = createContext({user: new User(), refresh: () => {}});
