```Z
/* This is an example of the birthday book. */

zed [ NAME, DATE ] end

tag 1
schema BirthdayBook
    known : \power NAME
    birthday : NAME \pfun DATE
where
    known = dom birthday
end

schema AddBirthday
    \Delta BirthdayBook
    name? : NAME
    date? : DATE
where
    name? \notin known
    birthday′ = birthday \cup { name? \mapsto date? }
end

schema FindBirthday
    \Xi BirthdayBook
    name? : NAME
    date! : DATE
where
    name? \in known
    date! = birthday ( name? )
end

schema Remind
	\Xi BirthdayBook
    today? : DATE
    cards! : \power NAME
where
    cards! = { n : known | birthday ( n ) = today? }
end

schema InitBirthdayBook
    BirthdayBook′
where
    known′ = { }
end

zed
   REPORT ::= ok | already_known | not_known
end

schema Success
    result! : REPORT
where
    result! = ok
end

schema AlreadyKnown
    \Xi BirthdayBook
    name? : NAME
    result! : REPORT
where
    name? \in known
    result! = already_known
end

zed
    RAddBirthday == ( AddBirthday \land Success ) \lor AlreadyKnown
end

schema NotKnown
    \Xi BirthdayBook
    name? : NAME
    result! : REPORT
where
    name? \notin known
    result! = not_known
end

zed
    RFindBirthday == ( FindBirthday \land Success ) \lor NotKnown
end

zed
    RRemind == Remind \land Success
end
```