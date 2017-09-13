#include "stdafx.h"
#include <iostream>
#include <stdexcept>
#include <string>

using namespace std;

static const int ARGS_COUNT = 4;
static const char DELIMETER = ',';

bool InvalidValue(string arg)
{
	bool valid = true;
	int delimeterCounter = 0;
	for (size_t i = 0; i < arg.size(); ++i)
	{
		if (!isdigit(arg[i]))
		{
			if (arg[i] == DELIMETER && delimeterCounter < 1)
			{
				++delimeterCounter;
			}
			else
			{
				valid = false;
				break;
			}
		}
	}

	return !valid || stod(arg) <= 0;
}

void CheckTriangleType(double a, double b, double c)
{
	if (a + b <= c || a + c <= b || b + c <= a)
	{
		cout << "Не треугольник" << endl;
	}
	else if (a == b && a == c)
	{
		cout << "Равносторонний треугольник." << endl;
	}
	else if (a == b || a == c || b == c)
	{
		cout << "Равнобедренный треугольник" << endl;
	}
	else
	{
		cout << "Обычный треугольник" << endl;
		
	}
}

int main(int argc, char *argv[])
{
	setlocale(LC_ALL, "Rus");
	try
	{
		if (argc != ARGS_COUNT)
		{
			throw logic_error("Некорректное количество аргументов. Пример: triangle.exe 1 1 1");
		}

		for (int i = 1; i < argc; ++i)
		{
			if (InvalidValue(argv[i]))
			{
				throw invalid_argument("Введено некорректное значение. Значения сторон должны быть положительными числами больше нуля.");
			}
		}
		
		double a = stod(argv[1]);
		double b = stod(argv[2]);
		double c = stod(argv[3]);

		CheckTriangleType(a, b, c);
	}
	catch (exception &err)
	{
		cout << err.what() << endl;
		return 1;
	}
    return 0;
}

