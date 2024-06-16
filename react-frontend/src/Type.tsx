export type User = {
    id: string;
    name: string;
    email: string;
}

export type Membership = {
    id: string;
    user_id: string;
    role: string;
    user: User;
}

export type MembershipList = {
    memberships: Membership[];
}

export type UserList = {
    users: User[];
}
