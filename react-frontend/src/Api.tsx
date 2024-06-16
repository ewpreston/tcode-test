import { MembershipList, UserList } from "./Type";

export const fetchMembers = async (): Promise<MembershipList> => {
    return fetch('http://localhost:8040/api/course/members')
        .then(res => res.json());
}

export const fetchUsers = async (): Promise<UserList> => {
    return fetch('http://localhost:8040/api/course/users')
        .then(res => res.json());
}