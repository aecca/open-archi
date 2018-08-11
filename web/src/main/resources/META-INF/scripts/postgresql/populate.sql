/* ARCHITECTURE */

INSERT INTO diagrams.size (id,
                           width,
                           height,
                           meta_id)
VALUES ('1f9eecfc-3f25-4edc-bdc2-0ad7576945ac', 250, 60, NULL);

INSERT INTO diagrams.elementshape (id,
                                   meta_id,
                                   type,
                                   size_id,
                                   fill,
                                   stroke,
                                   input,
                                   output,
                                   figure,
                                   "group")
VALUES
  ('061d46ad-7c6f-477c-a16f-3900dcacce57',
   NULL,
   'ARCHITECTURE_MODEL',
   '1f9eecfc-3f25-4edc-bdc2-0ad7576945ac',
   'White',
   'DargSlateGray',
   TRUE,
   TRUE,
   '',
   TRUE);

/* LAYER */

INSERT INTO diagrams.size (id,
                           width,
                           height,
                           meta_id)
VALUES ('56e5eea2-8451-4cea-8975-d72b0e131f71', 200, 40, NULL);

INSERT INTO diagrams.elementshape (id,
                                   meta_id,
                                   type,
                                   size_id,
                                   fill,
                                   stroke,
                                   input,
                                   output,
                                   figure,
                                   "group")
VALUES
  ('713af8da-0dca-4419-9cfd-b842438392d8',
   NULL,
   'LAYER',
   '56e5eea2-8451-4cea-8975-d72b0e131f71',
   'LimeGreen',
   'DarkOliveGreen',
   TRUE,
   TRUE,
   '',
   TRUE);

/* SYSTEM */

INSERT INTO diagrams.size (id,
                           width,
                           height,
                           meta_id)
VALUES ('6a8fec30-cee3-4e95-86d6-842971b767e2', 250, 60, NULL);

INSERT INTO diagrams.elementshape (id,
                                   meta_id,
                                   type,
                                   size_id,
                                   fill,
                                   stroke,
                                   input,
                                   output,
                                   figure,
                                   "group")
VALUES
  ('9019d959-8ffe-4615-a9bb-f46588d7406d',
   NULL,
   'SYSTEM',
   '6a8fec30-cee3-4e95-86d6-842971b767e2',
   '#02172C',
   'transparent',
   TRUE,
   TRUE,
   '',
   TRUE);

/* CONTAINER */

INSERT INTO diagrams.size (id,
                           width,
                           height,
                           meta_id)
VALUES ('bebb4640-5dfc-4fad-8634-333b15e69028', 250, 60, NULL);

INSERT INTO diagrams.elementshape (id,
                                   meta_id,
                                   type,
                                   size_id,
                                   fill,
                                   stroke,
                                   input,
                                   output,
                                   figure,
                                   "group")
VALUES
  ('6222894c-d307-49dd-97cf-bf95938bc9b8',
   NULL,
   'CONTAINER',
   'bebb4640-5dfc-4fad-8634-333b15e69028',
   '#08427B',
   'transparent',
   TRUE,
   TRUE,
   '',
   TRUE);

/* COMPONENT */

INSERT INTO diagrams.size (id,
                           width,
                           height,
                           meta_id)
VALUES ('b33da9f3-132e-47d4-9428-0d98aafd0c77', 25, 15, NULL);

INSERT INTO diagrams.elementshape (id,
                                   meta_id,
                                   type,
                                   size_id,
                                   fill,
                                   stroke,
                                   input,
                                   output,
                                   figure,
                                   "group")
VALUES
  ('c92ce01e-9c32-4aa5-ada7-a8edc0c546fd',
   NULL,
   'COMPONENT',
   'b33da9f3-132e-47d4-9428-0d98aafd0c77',
   '#1368BD',
   'transparent',
   TRUE,
   TRUE,
   '',
   FALSE);
