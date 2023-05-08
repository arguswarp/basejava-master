create table public.resume
(
    uuid      char(36) not null
        constraint resume_pk
            primary key,
    full_name text not null
);

alter table public.resume
    owner to argus;

create table public.contact
(
    id          integer generated by default as identity
        constraint contact_pk
            primary key,
    resume_uuid char(36) not null
        constraint contact_resume_uuid_fk
            references public.resume
            on delete cascade,
    type        text     not null,
    value       text     not null

);

alter table public.contact
    owner to argus;

create unique index contact_uuid_type_index
    on public.contact (resume_uuid, type);

create table public.section
(
    id          integer generated by default as identity
        constraint section_pk
            primary key,
    resume_uuid char(36)
        constraint section_resume_uuid_fk
            references public.resume
            on delete cascade,
    type        text not null,
    value       text not null
);

alter table public.section
    owner to argus;




