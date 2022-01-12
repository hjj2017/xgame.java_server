@echo off

rd /s/q .\out
md .\out

rem # Java
rem protoc --java_out=.\out .\characterProtocol.proto
rem protoc --java_out=.\out .\chatProtocol.proto
protoc --java_out=.\out .\commProtocol.proto
rem protoc --java_out=.\out .\fightProtocol.proto
rem protoc --java_out=.\out .\passportProtocol.proto
rem protoc --java_out=.\out .\worldProtocol.proto

rem # CShart
rem protoc --csharp_out=.\out .\characterProtocol.proto
rem protoc --csharp_out=.\out .\chatProtocol.proto
protoc --csharp_out=.\out .\commProtocol.proto
rem protoc --csharp_out=.\out .\fightProtocol.proto
rem protoc --csharp_out=.\out .\passportProtocol.proto
rem protoc --csharp_out=.\out .\worldProtocol.proto
